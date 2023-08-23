package com.kamancho.melisma.app.core

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kamancho.melisma.databinding.FavoritesFragmentBinding
import com.kamancho.melisma.favorites.presentation.FavoritesUiState
import com.simform.refresh.SSPullToRefreshLayout
import kotlinx.coroutines.launch

/**
 * Created by HP on 14.07.2023.
 **/
abstract class FavoritesFragment<T>(layout: Int): Fragment(layout) {

    protected lateinit var favoritesViewModel: FavoritesViewModel<FavoritesUiState,T>

    protected lateinit var adapter: Mapper<List<T>,Unit>

    protected val binding by viewBinding(FavoritesFragmentBinding::bind)

    private lateinit var textWatcher: TextWatcher

    companion object{
         const val loading_animation = "loading.json"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        textWatcher = object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                search(text.toString())
            }

            override fun afterTextChanged(p0: Editable?) = Unit
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pullToRefresh.setRepeatCount(SSPullToRefreshLayout.RepeatCount.INFINITE)
        binding.pullToRefresh.setLottieAnimation(loading_animation)
        binding.pullToRefresh.setRefreshViewParams(
            ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            250,
        ))

        binding.scrollUpButton.setupWithRecycler(binding.favoritesRcv)
        (binding.favoritesRcv.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

        lifecycleScope.launch{
            favoritesViewModel.collectLoading(this@FavoritesFragment){
                it.apply(
                    binding.errorMessage,
                    binding.pullToRefresh,
                    binding.favoritesRcv,
                )
            }
        }

        lifecycleScope.launch {
            favoritesViewModel.collectState(this@FavoritesFragment) {
                it.apply(
                    binding.errorMessage,
                    binding.pullToRefresh,
                    binding.favoritesRcv,
                )
            }
        }

        lifecycleScope.launch {
            favoritesViewModel.collectData(this@FavoritesFragment) {
                val state = binding.favoritesRcv.layoutManager?.onSaveInstanceState()
                adapter.map(it)
                binding.favoritesRcv.layoutManager?.onRestoreInstanceState(state)
                additionalActionsOnRecyclerUpdate(it)
            }
        }


        binding.pullToRefresh.setOnRefreshListener{
            favoritesViewModel.update(true)
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onStart() {
        binding.searchFavorites.addTextChangedListener(textWatcher)
        super.onStart()
    }


    override fun onStop() {
        binding.searchFavorites.removeTextChangedListener(textWatcher)
        super.onStop()
    }

    protected abstract fun additionalActionsOnRecyclerUpdate(data: List<T>)

    protected abstract fun search(query:String)
}