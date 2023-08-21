package com.example.musicapp.main.presentation

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.musicapp.R
import com.example.musicapp.app.core.ConnectionChecker
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.main.di.App
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.player.di.PlayerModule
import com.example.musicapp.player.presentation.PlayerFragment
import com.example.musicapp.queue.presenatation.QueueFragment
import com.example.musicapp.searchhistory.presentation.ViewPagerFragmentsAdapter
import com.example.musicapp.vkauth.presentation.AuthActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@UnstableApi class MainActivity : FragmentActivity() {

    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var viewModel: MainViewModel

    private lateinit var bottomSheet: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragments = listOf(PlayerFragment(), QueueFragment())

        binding.bottomSheet.bottomSheetVp.adapter =
            ViewPagerFragmentsAdapter(supportFragmentManager,lifecycle, fragments)


        bottomSheet = BottomSheetBehavior.from(binding.bottomSheet.root).apply {
            peekHeight = 0
        }

        (this.application as App).appComponent.inject(this)
        viewModel = ViewModelProvider(
            this,
            factory
        )[MainViewModel::class.java]

        val navHost = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHost.navController
        binding.bottomNavView.setupWithNavController(navController)




        bottomSheet.addBottomSheetCallback(object :BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if(newState == BottomSheetBehavior.STATE_COLLAPSED)
                    viewModel.bottomSheetState(BottomSheetBehavior.STATE_COLLAPSED)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit

        })

        lifecycleScope.launch{
            viewModel.collectActivityNavigationCommunication(this@MainActivity){
                it.apply(this@MainActivity,viewModel)
            }
        }

        lifecycleScope.launch {
            viewModel.collectBottomSheetState(this@MainActivity){
                bottomSheet.state = it
            }
        }

        lifecycleScope.launch {
            viewModel.collectPlayerControls(this@MainActivity) {
                it.apply(
                    binding,
                    imageLoader,
                    viewModel
                )
            }
        }

        lifecycleScope.launch{
            viewModel.collectSingleUiUpdateCommunication(this@MainActivity){
                it.apply(supportFragmentManager,this@MainActivity, binding)
            }
        }


        lifecycleScope.launch {
            viewModel.collectSlideViewPagerCommunication(this@MainActivity){
                binding.bottomSheet.bottomSheetVp.setCurrentItem(it,true)
            }
        }

        lifecycleScope.launch {
            viewModel.collectPermissionCheckCommunication(this@MainActivity){
                it.apply(this@MainActivity)
            }
        }


        binding.playBtn.setOnClickListener {
            if ((it as ToggleButton).isChecked)
                viewModel.playerAction(PlayerCommunicationState.Pause)
            else
                viewModel.playerAction(PlayerCommunicationState.Resume)
        }

        binding.previousBtn.setOnClickListener {
            viewModel.playerAction(PlayerCommunicationState.Previous)
        }

        binding.nextBtn.setOnClickListener {
            viewModel.playerAction(PlayerCommunicationState.Next)
        }

        binding.bottomPlayerBar.setOnClickListener {
            viewModel.bottomSheetState(BottomSheetBehavior.STATE_EXPANDED)
        }


        binding.bottomNavView.setOnItemSelectedListener {
            NavigationUI.onNavDestinationSelected(it,navController)
            return@setOnItemSelectedListener true
        }

        onBackPressedDispatcher.addCallback(this){
            if(bottomSheet.state != BottomSheetBehavior.STATE_COLLAPSED &&
                bottomSheet.state != BottomSheetBehavior.STATE_HIDDEN)
                viewModel.bottomSheetState(BottomSheetBehavior.STATE_COLLAPSED)
            else if(navController.backQueue.size > minimal_back_stack_size){
                navController.popBackStack()
            }else finish()
        }

    }

    companion object{
        private const val minimal_back_stack_size = 2
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==MainViewModel.permissionRequestCode && grantResults[0] != PackageManager.PERMISSION_GRANTED){
            viewModel.dontShowPermission()
        }
    }



}