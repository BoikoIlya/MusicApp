package com.kamancho.melisma.editplaylist.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.creteplaylist.di.PlaylistDataComponent
import com.kamancho.melisma.creteplaylist.presentation.PlaylistDataFragment
import com.kamancho.melisma.main.di.App
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 18.07.2023.
 **/
class EditPlaylistFragment: PlaylistDataFragment(R.layout.playlist_data_fragment) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var component: PlaylistDataComponent

    private lateinit var viewModel: EditPlaylistViewModel

    private lateinit var textWatcher: TextWatcher

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component = (context.applicationContext as App).appComponent
            .playlistDataComponent().build()
        component.inject(this)

        viewModel = ViewModelProvider(this, factory)[EditPlaylistViewModel::class.java]
        baseViewModel = viewModel
        baseImageLoader = imageLoader

        textWatcher = object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable){
                viewModel.verify(binding.titleEditText.text.toString(),editable.toString())
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.titleTv.setText(R.string.edit_playlist)

       val mapper = PlaylistUi.ToEditPlaylistData(binding)

        lifecycleScope.launch{
            viewModel.collectPlaylistDataCommunication(this@EditPlaylistFragment){
                viewModel.setupInitialData(it)
                it.map(mapper)
            }
        }

        lifecycleScope.launch{
            viewModel.collectTitleStateCommunication(this@EditPlaylistFragment){
                it.apply(binding)
            }
        }

        binding.addTracksBtn.setOnClickListener {
            findNavController().navigate(R.id.action_editPlaylistFragment_to_addToPlaylistFragment)
        }

        binding.error.reloadBtn.setOnClickListener {
            viewModel.updateData()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun titleTextChanged(title: String) {
        viewModel.verify(title,binding.descriptionEditText.text.toString())
    }

    override fun additionalActionOnRecyclerUpdate() {
        viewModel.verify(binding.titleEditText.text.toString(),binding.descriptionEditText.text.toString())
    }

    override fun onStart() {
        binding.descriptionEditText.addTextChangedListener(textWatcher)
        super.onStart()
    }


    override fun onStop() {
        binding.descriptionEditText.removeTextChangedListener(textWatcher)
        super.onStop()
    }
}