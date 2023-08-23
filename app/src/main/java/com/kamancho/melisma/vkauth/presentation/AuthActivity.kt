package com.kamancho.melisma.vkauth.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.kamancho.melisma.databinding.ActivityAuthBinding
import com.kamancho.melisma.main.di.App
import com.kamancho.melisma.vkauth.di.AuthComponent
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

        val intent = Intent(Intent.ACTION_VIEW)

        binding.forgotPassword.setOnClickListener {
            intent.data = Uri.parse(forgot_password_web_url)
            startActivity(intent)
        }

        binding.createAccount.setOnClickListener {
            intent.data = Uri.parse(create_vk_account_web_url)
            startActivity(intent)
        }
    }


    companion object{
        private const val create_vk_account_web_url = "https://id.vk.com/auth?v=1.46.0&app_id=7934655&uuid=28523cb43d&redirect_uri=https%3A%2F%2Fm.vk.com%2Fjoin&app_settings=W10%3D&action=eyJuYW1lIjoibm9fcGFzc3dvcmRfZmxvdyIsInBhcmFtcyI6eyJ0eXBlIjoic2lnbl91cCJ9fQ%3D%3D&scheme=bright_light"
        private const val forgot_password_web_url = "https://id.vk.com/restore/#/resetPassword"
    }
}