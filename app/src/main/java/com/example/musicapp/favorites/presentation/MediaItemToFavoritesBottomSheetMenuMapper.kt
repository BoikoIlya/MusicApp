package com.example.musicapp.favorites.presentation

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import com.example.musicapp.R
import com.example.musicapp.app.core.Mapper
import com.example.musicapp.app.core.ToMediaItemMapper.Base.Companion.is_cached
import com.example.musicapp.databinding.FavoritesMenuDialogBottomSheetBinding

/**
 * Created by HP on 09.08.2023.
 **/
interface MediaItemToFavoritesBottomSheetMenuMapper: Mapper<MediaItem, Unit> {

    class Base(
        private val binding: FavoritesMenuDialogBottomSheetBinding,
        private val context: Context,
        private val viewModel: FavoritesBottomSheetMenuViewModel,
        private val dismiss:()->Unit
    ): MediaItemToFavoritesBottomSheetMenuMapper{

        override fun map(data: MediaItem) {
           val drawableAndTintColor = if(data.mediaMetadata.extras!!.getBoolean(is_cached)){
                binding.cacheOption.text = context.getString(R.string.remove_from_downloads)
                binding.cacheOption.setOnClickListener {
                    viewModel.removeFromDownloads()
                    dismiss.invoke()
                }
               Pair(
                    ContextCompat.getDrawable(context,R.drawable.bin),
                    ContextCompat.getColorStateList(context, R.color.red)
               )
            }else{
                binding.cacheOption.text = context.getString(R.string.download )
               binding.cacheOption.setOnClickListener {
                   viewModel.download()
                    dismiss.invoke()
               }
               Pair(
                    ContextCompat.getDrawable(context,R.drawable.download),
                    ContextCompat.getColorStateList(context, R.color.green)
               )
            }

            binding.cacheOption.setCompoundDrawablesWithIntrinsicBounds(drawableAndTintColor.first, null, null, null)
            binding.cacheOption.compoundDrawableTintList = drawableAndTintColor.second

        }

    }
}