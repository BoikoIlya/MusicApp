package com.kamancho.melisma.deletetrackfromplaylist.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.kamancho.melisma.R
import com.kamancho.melisma.deletetrackfromplaylist.di.DeleteTrackFromPlaylistComponent
import com.kamancho.melisma.main.di.App
import javax.inject.Inject

/**
 * Created by HP on 26.06.2023.
 **/
class DeleteTrackFromPlaylistDialogFragment : DialogFragment() {

    private lateinit var viewModel: DeleteTrackFromPlaylistViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var component: DeleteTrackFromPlaylistComponent

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        component =  (context?.applicationContext as App).appComponent.playlistsComponent().build()
            .playlistDetailsComponent().build().deleteTrackFromPlaylistComponent().build()
          component.inject(this)
        viewModel = ViewModelProvider(requireActivity(), factory)[DeleteTrackFromPlaylistViewModel::class.java]

        val clickListener = DialogInterface.OnClickListener { _, buttonId ->
            when(buttonId){
                DialogInterface.BUTTON_POSITIVE -> viewModel.deleteTrackFromPlaylist ()
                DialogInterface.BUTTON_NEGATIVE -> viewModel.resetSwipedItem()
            }
        }

        val dialog= AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setMessage(R.string.delete_track_from_playlist_confirm_message)
            .setPositiveButton(R.string.yes_btn_text,clickListener)
            .setNegativeButton(R.string.no_btn_text,clickListener)
            .create()

        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_corners_shape)
        return dialog
    }



    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        viewModel.resetSwipedItem()
    }

    companion object{
        private val PLAYLIST_ID = "PLAYLIST_ID"
        private val AUDIO_ID = "AUDIO_ID"

        fun newInstance(playlistId:Int,audioId: Int): DeleteTrackFromPlaylistDialogFragment{
            val bundle = Bundle()
            bundle.putInt(PLAYLIST_ID,playlistId)
            bundle.putInt(AUDIO_ID,audioId)
            val fragment = DeleteTrackFromPlaylistDialogFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}