package com.example.musicapp.settings.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicapp.BuildConfig
import com.example.musicapp.R
import com.example.musicapp.databinding.SettingsFragmentBinding
import com.example.musicapp.main.di.App
import com.example.musicapp.main.di.AppComponent
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 22.08.2023.
 **/
class SettingsFragment: Fragment(R.layout.settings_fragment) {

    private val binding by viewBinding(SettingsFragmentBinding::bind)

    private lateinit var viewModel: SettingsViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var component: AppComponent

    private val requestOpenDocumentTree = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
        if (uri != null) {

           viewModel.saveNewDownloadsPath(
                Environment.getExternalStoragePublicDirectory(uri.path).path
           )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component = (context.applicationContext as App).appComponent
        component.inject(this)
        viewModel = ViewModelProvider(this, factory)[SettingsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appVersion.text = BuildConfig.VERSION_NAME

        lifecycleScope.launch {
            viewModel.collectDownloadsPath(this@SettingsFragment){
                binding.downloadsFolderPath.text = it
            }
        }

        lifecycleScope.launch {
            viewModel.collectDownloadsSize(this@SettingsFragment){
                binding.deleteDownloadedTracksSize.text = it
            }
        }

        binding.logoutBtn.setOnClickListener {
           viewModel.logoutDialog()
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.downloadsFolder.setOnClickListener{
            requestOpenDocumentTree.launch(binding.downloadsFolderPath.text.toString().toUri())
        }

        binding.telegram.setOnClickListener {
            try {
                val telegram = Intent(Intent.ACTION_SEND)
                telegram.data = Uri.parse(telegramChannelUrl)
                telegram.setPackage(telegramPackageName)
                startActivity(telegram)
            }catch (e: Exception){
                val telegram = Intent(Intent.ACTION_VIEW)
                telegram.data = Uri.parse(telegramChannelUrl)
                startActivity(telegram)
            }
        }

        binding.deleteDownloadedTracks.setOnClickListener {
            viewModel.deleteDownloadedTracksDialog()
        }
    }

    companion object{
        private const val telegramChannelUrl = "https://t.me/melismaApp"
        private const val telegramPackageName = "org.telegram.messenger"
    }
}