package com.kamancho.melisma.vkauth.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.kamancho.melisma.R
import com.kamancho.melisma.databinding.ActivityAuthBinding
import com.kamancho.melisma.main.di.App
import com.kamancho.melisma.vkauth.di.AuthComponent
import kotlinx.coroutines.launch
import javax.inject.Inject


class AuthActivity : FragmentActivity() {

    private lateinit var binding: ActivityAuthBinding

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var authComponent: AuthComponent

    private lateinit var viewModel: MainAuthViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState,)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authComponent = (this.applicationContext as App).appComponent.authComponent().build()
        authComponent.inject(this)

        viewModel = ViewModelProvider(this, factory)[MainAuthViewModel::class.java]

        val navHost = supportFragmentManager.findFragmentById(R.id.auth_fragment_container) as NavHostFragment
        val navController = navHost.navController

        lifecycleScope.launch{
            viewModel.collectSingleAuthCommunication(this@AuthActivity){
                it.apply(viewModel,navController,this@AuthActivity,)
            }
        }

    }

}