package com.kamancho.melisma.vkauth.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kamancho.melisma.R
import com.kamancho.melisma.databinding.AuthFragmentBinding
import com.kamancho.melisma.databinding.TrendingFragmentBinding
import com.kamancho.melisma.main.di.App
import com.kamancho.melisma.vkauth.di.AuthComponent
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 16.09.2023.
 **/
class AuthFragment : Fragment(R.layout.auth_fragment) {


    private val binding by viewBinding(AuthFragmentBinding::bind)

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var authComponent: AuthComponent

    private lateinit var viewModel: AuthViewModel


    override fun onAttach(context: Context) {
        super.onAttach(context)
        authComponent = (context.applicationContext as App).appComponent.authComponent().build()
        authComponent.inject(this)

        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null){
            MaterialAlertDialogBuilder(requireContext())
                .setMessage(R.string.auth_suggest)
                .create()
                .show()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        lifecycleScope.launch {
            viewModel.collectAuthState(this@AuthFragment) {
                it.apply(binding, this@AuthFragment.requireActivity())
            }
        }


        binding.loginBtn.setOnClickListener {
            viewModel.submit(binding.loginEdt.text.toString(), binding.passwordEdt.text.toString())
        }

        val intent = Intent(Intent.ACTION_VIEW)

        binding.forgotPassword.setOnClickListener {
            intent.data = Uri.parse(forgot_password_web_url)
            startActivity(intent)
        }

        binding.createAccount.setOnClickListener {
            intent.data = Uri.parse(create_vk_account_web_url)
            startActivity(intent)
        }

        binding.alternativeAuth.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(
                SingleAuthState.LaunchRedirection.redirection_url,
                alternative_auth_marusya_url
            )
            findNavController().navigate(R.id.action_authFragment_to_authWebViewFragment, bundle)
        }
    }

    companion object {
        private const val create_vk_account_web_url =
            "https://id.vk.com/auth?v=1.46.0&app_id=7934655&uuid=28523cb43d&redirect_uri=https%3A%2F%2Fm.vk.com%2Fjoin&app_settings=W10%3D&action=eyJuYW1lIjoibm9fcGFzc3dvcmRfZmxvdyIsInBhcmFtcyI6eyJ0eXBlIjoic2lnbl91cCJ9fQ%3D%3D&scheme=bright_light"
        private const val forgot_password_web_url = "https://id.vk.com/restore/#/resetPassword"
        private const val alternative_auth_marusya_url =
            "https://oauth.vk.com/authorize?client_id=6463690&scope=1073737727&redirect_uri=https://oauth.vk.com/blank.html&display=page&response_type=token&revoke=1"
    }



}