package com.example.musicapp.main.presentation

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.ToggleButton
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.core.os.BuildCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.R
import com.example.musicapp.app.core.ClickListener
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.app.core.Selector
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.main.di.App
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.player.di.PlayerModule
import com.example.musicapp.updatesystem.presentation.FCMUpdateService
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@UnstableApi class MainActivity : FragmentActivity() {

    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var viewModel: MainViewModel

    private lateinit var bottomSheet: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        bottomSheet = BottomSheetBehavior.from(binding.bottomSheet).apply {
            peekHeight = 0
            if(intent.getBooleanExtra(PlayerModule.ACTION_SONG_ACT,false))
                this.state = BottomSheetBehavior.STATE_EXPANDED
            else
                this.state = BottomSheetBehavior.STATE_HIDDEN
        }


        (this.application as App).appComponent.inject(this)
        viewModel = ViewModelProvider(
            this,
            factory
        )[MainViewModel::class.java]

        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHost.navController
        binding.bottomNavView.setupWithNavController(navController)


        binding.bottomSheetVp.adapter = ScreenSlidePagerAdapter(this)

        bottomSheet.addBottomSheetCallback(object :BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if(newState == BottomSheetBehavior.STATE_COLLAPSED)
                    viewModel.bottomSheetState(BottomSheetBehavior.STATE_COLLAPSED)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit

        })



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

        lifecycleScope.launch{
            viewModel.collectSlideViewPagerIndex(this@MainActivity){
                binding.bottomSheetVp.currentItem = it
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

    override fun onResume() {
        super.onResume()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            viewModel.notificationPermissionCheck()
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