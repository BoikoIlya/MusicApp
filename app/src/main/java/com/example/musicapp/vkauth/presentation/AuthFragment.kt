package com.example.musicapp.vkauth.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentAuthBinding
import com.example.musicapp.main.di.App
import com.example.musicapp.vkauth.di.AuthComponent
import kotlinx.coroutines.launch
import javax.inject.Inject


class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val binding by viewBinding(FragmentAuthBinding::bind)

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       lifecycleScope.launch{
           viewModel.collectAuthState(this@AuthFragment){
               it.apply(binding, childFragmentManager)
           }
       }


        binding.loginBtn.setOnClickListener {
            viewModel.submit(binding.loginEdt.text.toString(),binding.passwordEdt.text.toString())
        }
    }

}