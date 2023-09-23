package com.kamancho.melisma.vkauth.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kamancho.melisma.R
import com.kamancho.melisma.databinding.AuthFragmentBinding
import com.kamancho.melisma.databinding.AuthWebViewFragmentBinding
import com.kamancho.melisma.main.di.App
import com.kamancho.melisma.vkauth.di.AuthComponent
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 16.09.2023.
 **/
class AuthWebViewFragment : Fragment(R.layout.auth_web_view_fragment) {


    private val binding by viewBinding(AuthWebViewFragmentBinding::bind)

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var authComponent: AuthComponent

    private lateinit var viewModel: AuthViewModel

    val args:AuthWebViewFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        authComponent = (context.applicationContext as App).appComponent.authComponent().build()
        authComponent.inject(this)

        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.authWebView.webViewClient = AuthWebViewClient(viewModel)
        binding.authWebView.loadUrl(args.redirectionUrl)

    }
}