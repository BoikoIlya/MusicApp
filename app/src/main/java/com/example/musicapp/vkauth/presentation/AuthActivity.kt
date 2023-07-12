package com.example.musicapp.vkauth.presentation

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.musicapp.databinding.ActivityAuthBinding
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.main.di.App
import com.example.musicapp.vkauth.di.AuthComponent
import kotlinx.coroutines.launch
import javax.inject.Inject


class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var authComponent: AuthComponent

    private lateinit var viewModel: AuthViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState,)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authComponent = (this.applicationContext as App).appComponent.authComponent().build()
        authComponent.inject(this)

        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        lifecycleScope.launch{
            viewModel.collectAuthState(this@AuthActivity){
                it.apply(binding, this@AuthActivity)
            }
        }


        binding.loginBtn.setOnClickListener {
            viewModel.submit(binding.loginEdt.text.toString(),binding.passwordEdt.text.toString())
        }

    }


}