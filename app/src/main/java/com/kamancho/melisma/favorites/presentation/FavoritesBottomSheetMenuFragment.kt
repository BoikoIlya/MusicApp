package com.kamancho.melisma.favorites.presentation

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kamancho.melisma.R
import com.kamancho.melisma.databinding.FavoritesMenuDialogBottomSheetBinding
import com.kamancho.melisma.main.di.App
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

/**
 * Created by HP on 19.07.2023.
 **/
class FavoritesBottomSheetMenuFragment : BottomSheetDialogFragment(R.layout.favorites_menu_dialog_bottom_sheet) {

    private val args: FavoritesBottomSheetMenuFragmentArgs by navArgs()

    private lateinit var viewModel: FavoritesBottomSheetMenuViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory


    private val binding by viewBinding(FavoritesMenuDialogBottomSheetBinding::bind)

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireContext().applicationContext as App).appComponent.favoriteComponent().build()
            .inject(this)

        viewModel = ViewModelProvider(
            requireActivity(),
            factory
        )[FavoritesBottomSheetMenuViewModel::class.java]
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addToPlaylistOption.isVisible = args.enableAddToPlaylist

        val drawableAndTintColor = if(args.isCached){
            binding.downloadOption.text = requireContext().getString(R.string.remove_from_downloads)
            binding.downloadOption.setOnClickListener {
                viewModel.removeFromDownloads(args.trackTitle, args.trackArtist)
                dismiss()
            }
            Pair(
                ContextCompat.getDrawable(requireContext(),R.drawable.bin),
                ContextCompat.getColorStateList(requireContext(), R.color.red)
            )
        }else{
            binding.downloadOption.text = requireContext().getString(R.string.download )
            binding.downloadOption.setOnClickListener {
                viewModel.download(args.trackUri, args.trackTitle, args.trackArtist)
                dismiss()
            }
            Pair(
                ContextCompat.getDrawable(requireContext(),R.drawable.download),
                ContextCompat.getColorStateList(requireContext(), R.color.green)
            )
        }

        binding.downloadOption.setCompoundDrawablesWithIntrinsicBounds(drawableAndTintColor.first, null, null, null)
        binding.downloadOption.compoundDrawableTintList = drawableAndTintColor.second


        binding.addToPlaylistOption.setOnClickListener {
            findNavController().navigate(R.id.action_favoritesBottomSheetMenuFragment_to_selectPlaylistFragment)
        }

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.resetSwipedItem()
    }

    override fun onPause() {
        super.onPause()
        dialog?.window?.setWindowAnimations(-1)
    }
}