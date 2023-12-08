package com.kamancho.melisma.main.presentation

import android.animation.Animator.AnimatorListener
import android.animation.ValueAnimator
import android.app.DownloadManager
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.widget.ToggleButton
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.request.transition.ViewPropertyTransition.Animator
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.main.di.App
import com.kamancho.melisma.databinding.ActivityMainBinding
import com.kamancho.melisma.downloader.presentation.DownloadTrackBroadcastReceiver
import com.kamancho.melisma.player.presentation.PlayerFragment
import com.kamancho.melisma.queue.presenatation.QueueFragment
import com.kamancho.melisma.searchhistory.presentation.ViewPagerFragmentsAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.InstallStatus
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.update.UpdateManager
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
class MainActivity : FragmentActivity() {

    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var updateManager: UpdateManager

    private lateinit var viewModel: MainViewModel

    private lateinit var bottomSheet: BottomSheetBehavior<ConstraintLayout>

    private lateinit var downloadBroadcastReceiver: DownloadTrackBroadcastReceiver

    private lateinit var resultLauncher: ActivityResultLauncher<IntentSenderRequest>

    private val updateListener =
        InstallStateUpdatedListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                viewModel.sendSingleUiEvent(SingleUiEventState.ShowSnackBar.Success(getString(R.string.update_will_install)))
                updateManager.completeUpdate()
            } else if (state.installStatus() == InstallStatus.FAILED) {
                viewModel.sendSingleUiEvent(
                    SingleUiEventState.ShowSnackBar.Error(
                        String.format(getString(R.string.update_failed) + ", " + getString(R.string.code) + " " + state.installErrorCode())
                    )
                )
            }
        }

    companion object {
        private const val minimal_back_queue_size = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragments = listOf(PlayerFragment(), QueueFragment())
        downloadBroadcastReceiver = DownloadTrackBroadcastReceiver()

        binding.bottomSheet.bottomSheetVp.adapter =
            ViewPagerFragmentsAdapter(supportFragmentManager, lifecycle, fragments)


        bottomSheet = BottomSheetBehavior.from(binding.bottomSheet.root).apply {
            peekHeight = 0
        }

        (this.application as App).appComponent.inject(this)
        viewModel = ViewModelProvider(
            this,
            factory
        )[MainViewModel::class.java]

        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHost.navController
        binding.bottomNavView.setupWithNavController(navController)
        val badge = binding.bottomNavView.getOrCreateBadge(R.id.notificationsFragment)


        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED)
                    viewModel.bottomSheetState(BottomSheetBehavior.STATE_COLLAPSED)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit

        })

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult())
        { result ->
            if (result.resultCode != RESULT_OK && result.resultCode != RESULT_CANCELED)
                viewModel.sendSingleUiEvent(SingleUiEventState.ShowSnackBar.Error(getString(R.string.update_failed)))

        }

        lifecycleScope.launch {
            viewModel.collectActivityNavigationCommunication(this@MainActivity) {
                it.apply(this@MainActivity, viewModel)
                it.apply(resultLauncher, updateListener, updateManager)
            }
        }

        lifecycleScope.launch {
            viewModel.collectBottomSheetState(this@MainActivity) {
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

        lifecycleScope.launch {
            viewModel.collectSingleUiUpdateCommunication(this@MainActivity) {
                it.apply(supportFragmentManager, this@MainActivity, binding)
            }
        }


        lifecycleScope.launch {
            viewModel.collectSlideViewPagerCommunication(this@MainActivity) {
                binding.bottomSheet.bottomSheetVp.setCurrentItem(it, true)
            }
        }

        lifecycleScope.launch {
            viewModel.collectPermissionCheckCommunication(this@MainActivity) {
                it.apply(this@MainActivity)
            }
        }

        lifecycleScope.launch {
            viewModel.collectNotificationBadgeCommunication(this@MainActivity) {
                badge.isVisible = it
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




        onBackPressedDispatcher.addCallback(this) {
            if (bottomSheet.state != BottomSheetBehavior.STATE_COLLAPSED &&
                bottomSheet.state != BottomSheetBehavior.STATE_HIDDEN
            )
                viewModel.bottomSheetState(BottomSheetBehavior.STATE_COLLAPSED)
            else if (navController.currentBackStack.value.size >= minimal_back_queue_size) {
                navController.popBackStack()
            } else finish()
        }



        ContextCompat.registerReceiver(
            this,
            downloadBroadcastReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            ContextCompat.RECEIVER_EXPORTED
        )

        binding.lottieSanta.addAnimatorListener(object : AnimatorListener{
            override fun onAnimationStart(animation: android.animation.Animator) = Unit

            override fun onAnimationEnd(animation: android.animation.Animator) {
                val alphaAnimator = ValueAnimator.ofFloat(1f, 0f)
                alphaAnimator.duration = 300
                alphaAnimator.addUpdateListener { animation ->
                    binding.lottieSanta.alpha = animation.animatedValue as Float
                }
                alphaAnimator.addListener(onEnd = {
                    binding.lottieSanta.visibility = View.GONE
                    binding.lottieSanta.isEnabled = false
                })
                alphaAnimator.start()
            }

            override fun onAnimationCancel(animation: android.animation.Animator) = Unit

            override fun onAnimationRepeat(animation: android.animation.Animator) = Unit

        })
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isEmpty()) return

        if (
            requestCode == MainViewModel.permissionRequestCode &&
            grantResults[0] != PackageManager.PERMISSION_GRANTED
        )
            viewModel.dontShowPermission()
        else {
            viewModel.disablePermissionCheck()
            viewModel.reloadTracks()
        }

        viewModel.updateNotifications()
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(downloadBroadcastReceiver)
        updateManager.unregisterListener(updateListener)
    }


}


