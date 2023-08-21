package com.example.musicapp.captcha.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicapp.R
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.captcha.data.RepeatActionAfterCaptcha
import com.example.musicapp.databinding.CaptchaDialogFragmentBinding
import com.example.musicapp.main.di.App
import com.example.musicapp.player.presentation.DeleteTrackFromPlayerMenuDialogViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 27.07.2023.
 **/
class CaptchaFragmentDialog: DialogFragment(R.layout.captcha_dialog_fragment) {

    private val binding by viewBinding(CaptchaDialogFragmentBinding::bind)

    @Inject
    lateinit var imageLoader: ImageLoader

    lateinit var viewModel: CaptchaDialogViewModel

    private lateinit var action: RepeatActionAfterCaptcha
    private lateinit var url: String

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as App).appComponent.inject(this)
        viewModel = ViewModelProvider(requireActivity(), factory)[CaptchaDialogViewModel::class.java]
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(requireContext())
            .setView(R.layout.captcha_dialog_fragment)
            .show()
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_corners_shape)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.collectCaptchaData{
                url = it.first
                imageLoader.loadCaptcha(it.first,binding.captchaImg,
                    success = {
                        binding.loading.visibility = View.GONE
                        viewModel.setError(R.string.empty_string)
                        binding.captchaImg.isClickable = false
                        binding.submitBtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.black)
                        binding.submitBtn.isClickable = true
                    },
                    error = {
                        binding.loading.visibility = View.INVISIBLE
                        viewModel.setError(R.string.fail_to_load_captcha)
                        binding.captchaImg.isClickable = true
                    })
                action = it.second
            }
        }

        lifecycleScope.launch {
            viewModel.collectCaptchaError(this@CaptchaFragmentDialog){
                binding.captchaInputLayout.error = it
                binding.captchaInputLayout.isErrorEnabled = it.isNotEmpty()
            }
        }

        lifecycleScope.launch{
            viewModel.collectDismissDialog(this@CaptchaFragmentDialog){
                dismiss()
            }
        }

        binding.submitBtn.setOnClickListener {
            viewModel.submit(binding.captchaEdt.text.toString(),action)
        }

        binding.submitBtn.isClickable = false
        binding.captchaImg.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            imageLoader.loadCaptcha(url,binding.captchaImg,
                success = {
                    binding.loading.visibility = View.GONE
                    viewModel.setError(R.string.empty_string)
                    binding.captchaImg.isClickable = false
                    binding.submitBtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.black)
                    binding.submitBtn.isClickable = true
                },
                error = {
                    binding.loading.visibility = View.INVISIBLE
                    viewModel.setError(R.string.fail_to_load_captcha)
                    binding.captchaImg.isClickable = true
                })
        }
    }



}