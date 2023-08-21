package com.example.musicapp.player.presentation


import android.R.attr.animationDuration
import android.animation.ValueAnimator
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.musicapp.R
import com.example.musicapp.app.core.BlurEffectAnimator
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.big_img_url
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.small_img_url
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.track_duration_formatted
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.track_duration_in_millis
import com.example.musicapp.databinding.PlayerFragmentBinding
import com.example.musicapp.main.di.App
import com.example.musicapp.main.presentation.PlayerCommunicationState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import javax.inject.Inject


 /**
 * Created by HP on 22.04.2023.
 **/
class PlayerFragment: Fragment(R.layout.player_fragment) {

    private val binding by viewBinding(PlayerFragmentBinding::bind)

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var blurEffectAnimator: BlurEffectAnimator

    private lateinit var viewModel: PlayerViewModel

    private lateinit var currentTrack: MediaItem

    private var currentTrackId = 0


    override fun onAttach(context: Context) {
        super.onAttach(context)
         (context.applicationContext as App).appComponent.playerComponent().build()
            .inject(this)
        viewModel = ViewModelProvider(this, factory)[PlayerViewModel::class.java]
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.songName.isSelected = true
        binding.songAuthor.isSelected = true
        binding.albumName.isSelected = true


        lifecycleScope.launch {
            viewModel.collectSelectedTrack(this@PlayerFragment) {
                with(binding) {
                    totalDuration.text = it.mediaMetadata.extras?.getString(track_duration_formatted)?:""
                    val durationInMillis = it.mediaMetadata.extras?.getFloat(track_duration_in_millis)?:1f
                    binding.slider.value = 0f
                    binding.slider.valueTo = durationInMillis
                    viewModel.saveMaxDuration(durationInMillis)
                    with(it.mediaMetadata) {
                        albumName.text = albumTitle
                        imageLoader.loadImage(
                            extras?.getString(big_img_url)?:"",
                            songImg,
                            imgBg,
                            extras?.getString(small_img_url)?:""
                        )
                        songName.text = title
                        songAuthor.text = artist
                    }
                    currentTrack = it
                }
            }
        }

        lifecycleScope.launch {
            viewModel.collectPlayerControls(this@PlayerFragment) {
                it.apply(binding, viewModel)
            }
        }

        binding.shuffleSongBtn.isChecked = viewModel.isShuffleModeEnabled()
        binding.repeatSongBtn.isChecked = viewModel.isRepeatEnabled()


        lifecycleScope.launch {
            viewModel.collectTrackPosition(this@PlayerFragment) {
                binding.slider.value = it.first
                binding.currentPosition.text = it.second
            }
        }


        lifecycleScope.launch {
            viewModel.collectPlayingTrackIdCommunication(this@PlayerFragment) {
               currentTrackId = it
            }
        }

        binding.slider.addOnChangeListener(Slider.OnChangeListener { _, value, user ->
           if(user)  binding.currentPosition.text = viewModel.durationForTextView(value.toLong())
        })

        binding.slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener{
            override fun onStartTrackingTouch(slider: Slider) = Unit

            override fun onStopTrackingTouch(slider: Slider) {
                viewModel.playerAction(PlayerCommunicationState.SeekToPosition(slider.value.toLong()))
            }

        })



        binding.backBtn.setOnClickListener {
            viewModel.bottomSheetState(BottomSheetBehavior.STATE_COLLAPSED)
        }

        binding.playSongBtn.setOnClickListener {
            if ((it as ToggleButton).isChecked)
                viewModel.playerAction(PlayerCommunicationState.Pause)
            else
                viewModel.playerAction(PlayerCommunicationState.Resume)
        }

        binding.previousSongBtn.setOnClickListener {
            viewModel.playerAction(PlayerCommunicationState.Previous)
        }

        binding.nextSongBtn.setOnClickListener {
            viewModel.playerAction(PlayerCommunicationState.Next)
        }

        binding.shuffleSongBtn.setOnClickListener {
            if ((it as ToggleButton).isChecked)
                viewModel.playerAction(PlayerCommunicationState.ShuffleMode(PlayerCommunicationState.ShuffleMode.ENABLE_SHUFFLE))
            else
                viewModel.playerAction(PlayerCommunicationState.ShuffleMode(PlayerCommunicationState.ShuffleMode.DISABLE_SHUFFLE))
        }

        binding.repeatSongBtn.setOnClickListener {
            if ((it as ToggleButton).isChecked)
                viewModel.playerAction(PlayerCommunicationState.RepeatMode(Player.REPEAT_MODE_ONE))
            else
                viewModel.playerAction(PlayerCommunicationState.RepeatMode(Player.REPEAT_MODE_OFF))


        }

        binding.playerMenuBtn.setOnClickListener {
            blurEffectAnimator.show(view.rootView as View)
            val popup = PopupMenu(requireContext(), it, 0, 0, R.style.popupOverflowMenu)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                popup.setForceShowIcon(true)
            popup.menuInflater.inflate(R.menu.player_options, popup.menu)
            popup.show()

            popup.setOnDismissListener { blurEffectAnimator.hide(view.rootView as View) }
            popup.setOnMenuItemClickListener { menuItem ->

                when (menuItem.itemId) {
                    R.id.add_to_playlist_option -> viewModel.launchDeleteItemDialog(currentTrackId,currentTrack)
                    R.id.add_option -> viewModel.checkAndAddTrackToFavorites(currentTrack)
                }
                return@setOnMenuItemClickListener true
            }
        }


        binding.moveToQueue.setOnClickListener {
            viewModel.slidePage(1)
        }

        binding.songName.setOnLongClickListener {
            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val text = binding.songName.text.toString()+getString(R.string.dash)+binding.songAuthor.text.toString()
            val clipData= ClipData.newPlainText(text,text)
            clipboard.setPrimaryClip(clipData)
            viewModel.showSnackBar(SingleUiEventState.ShowSnackBar.Success(getString(R.string.copied)+text))
            return@setOnLongClickListener true
        }
    }
}


