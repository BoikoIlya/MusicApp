package com.example.musicapp.creteplaylist.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicapp.R
import com.example.musicapp.addtoplaylist.presentation.SelectedTrackUi
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.app.core.Selector
import com.example.musicapp.databinding.PlaylistDataFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

/**
 * Created by HP on 16.07.2023.
 **/
abstract class PlaylistDataFragment(layout: Int): Fragment(layout) {

    protected val binding by viewBinding(PlaylistDataFragmentBinding::bind)

    private lateinit var textWatcher: TextWatcher

    protected lateinit var baseViewModel: BasePlaylistDataViewModel

    protected lateinit var baseImageLoader: ImageLoader

    override fun onAttach(context: Context) {
        super.onAttach(context)
        textWatcher = object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable){
                titleTextChanged(editable.toString())
            }
        }
    }

    abstract fun titleTextChanged(title: String)

    abstract fun additionalActionOnRecyclerUpdate()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nestedSroll.minimumHeight = resources.displayMetrics.heightPixels
        binding.selectedTracksRcv.layoutManager = LinearLayoutManager(requireContext())

        val adapter = PlaylistDataSelectedTracksAdapter(
            context = requireContext(),
            clickListener = object : ClickListener<SelectedTrackUi> {
                override fun onClick(data: SelectedTrackUi) {
                    baseViewModel.removeItem(data)
                }
            },
            imageLoader = baseImageLoader,
            btnVisibility = View.VISIBLE
        )

        binding.selectedTracksRcv.adapter = adapter

        lifecycleScope.launch {
            baseViewModel.collectSelectedTracksCommunication(this@PlaylistDataFragment){
                adapter.map(it)
                additionalActionOnRecyclerUpdate()
            }
        }

        lifecycleScope.launch {
            baseViewModel.collectSaveButtonState(this@PlaylistDataFragment){
                it.apply(binding,requireContext())
            }
        }

        lifecycleScope.launch {
            baseViewModel.collectUiState(this@PlaylistDataFragment){
                it.apply(binding,findNavController())
            }
        }

        binding.saveBtn.setOnClickListener {
            baseViewModel.save(binding.titleEditText.text.toString(),binding.descriptionEditText.text.toString())
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onStart() {
        binding.titleEditText.addTextChangedListener(textWatcher)
        super.onStart()
    }


    override fun onStop() {
        binding.titleEditText.removeTextChangedListener(textWatcher)
        super.onStop()
    }
}