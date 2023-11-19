package com.kamancho.melisma.userplaylists.presentation

import android.app.AlertDialog
import android.content.Context
import android.os.Parcelable
import androidx.annotation.Keep
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.databinding.PlaylistBinding
import com.kamancho.melisma.databinding.PlaylistDataFragmentBinding
import com.kamancho.melisma.databinding.PlaylistDetailsTopbarBinding
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

/**
 * Created by HP on 13.07.2023.
 **/
@Keep
@Parcelize
data class PlaylistUi(
    private val playlistId: String,
    private val title: String,
    private val isFollowing: Boolean,
    private val count: Int,
    private val description: String,
    private val ownerId: Int,
    private val thumbStates: List<PlaylistThumbsState>
):Parcelable{

    fun map(item: PlaylistUi):Boolean =  playlistId == item.playlistId

    fun<T>map(mapper: Mapper<T>): T = mapper.map(playlistId, title, isFollowing, count, description, ownerId, thumbStates)

    fun handleId(): PlaylistUi {
        return if(playlistId.length>ownerId.toString().length && playlistId.contains(ownerId.toString())) {
            val actualPlaylistId = playlistId.replace(ownerId.toString(), "")
            this.copy(playlistId = actualPlaylistId)
        }else this
    }

    interface Mapper<T>{

        fun map(
            playlistId: String,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbStates: List<PlaylistThumbsState>
        ): T
    }


    class ShowToUiMapper(
        private val binding: PlaylistBinding,
        private val imageLoader: ImageLoader,
        private val cacheStrategy: DiskCacheStrategy
    ): Mapper<Unit>{

        private val imageViewList =  listOf(
            binding.leftImgUpper,
            binding.rightImgUpper,
            binding.rightImgLower,
            binding.leftImgLower)

        override fun map(
            playlistId: String,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbStates: List<PlaylistThumbsState>,
        ) {
            with(binding) {
                playlistName.text = title.take(50)
                var i = 0
                while (i < imageViewList.size) {
                    thumbStates[i].apply(imageViewList[i], imageLoader, cacheStrategy)
                    i++
                }
            }
        }




    }

    class ToDomainMapper @Inject constructor(): Mapper<PlaylistDomain> {

        override fun map(
            playlistId: String,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbStates: List<PlaylistThumbsState>,
        ): PlaylistDomain = PlaylistDomain(
            playlistId = playlistId,
            title = title,
            isFollowing = is_following,
            count = count,
            description = description,
            ownerId = owner_id,
            thumbs = thumbStates.map { it.map() }.filter { it!="" }

        )
    }

    class ToIdMapper @Inject constructor(): Mapper<String> {
        override fun map(
            playlistId: String,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbStates: List<PlaylistThumbsState>,
        ): String = playlistId
    }

    class ToOwnerIdMapper @Inject constructor(): Mapper<Int> {
        override fun map(
            playlistId: String,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbStates: List<PlaylistThumbsState>,
        ): Int = owner_id
    }

    interface ToTitleMapper: Mapper<String> {

        class Base @Inject constructor() : ToTitleMapper {
            override fun map(
                playlistId: String,
                title: String,
                is_following: Boolean,
                count: Int,
                description: String,
                owner_id: Int,
                thumbStates: List<PlaylistThumbsState>,
            ): String = title
        }

    }

    class IsPlaylistDataChanged(
        private val newTitle: String,
        private val newDescription: String,
    ): Mapper<Boolean> {
        override fun map(
            playlistId: String,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbStates: List<PlaylistThumbsState>,
        ): Boolean = newTitle.trim()!=title ||newDescription.trim() !=description

    }

    class ToEditPlaylistData(
        private val binding: PlaylistDataFragmentBinding
    ): Mapper<Unit> {
        override fun map(
            playlistId: String,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbStates: List<PlaylistThumbsState>,
        ){
            binding.titleEditText.setText(title)
            binding.descriptionEditText.setText(description)
        }

    }

    class CanEdit @Inject constructor(): Mapper<Boolean> {
        override fun map(
            playlistId: String,
            title: String,
            isFollowing: Boolean,
            count: Int,
            description: String,
            ownerId: Int,
            thumbStates: List<PlaylistThumbsState>,
        ): Boolean {
           return !isFollowing
        }
    }

    class ShowToUiPlaylistDetails(
        private val binding: PlaylistDetailsTopbarBinding,
        private val imageLoader: ImageLoader,
        private val context: Context,
        private val cacheStrategy: DiskCacheStrategy,
    ): Mapper<Unit>{

        private val imageViewList =  listOf(
            binding.leftImgUpper,
            binding.rightImgUpper,
            binding.rightImgLower,
            binding.leftImgLower)

        override fun map(
            playlistId: String,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbStates: List<PlaylistThumbsState>,
        ) = with(binding){
                titleTv.text = title.take(100)
                titleTv.isSelected = true
                descriptionTv.text = description.take(200)
                trackAmount.text = String.format(count.toString()+context.getString(R.string.tracks))
                var i = 0
                while (i<imageViewList.size){

                    thumbStates[i].apply(imageViewList[i],imageLoader, cacheStrategy)
                    i++
                }
                descriptionTv.setOnClickListener {
                    if (binding.descriptionTv.layout.getEllipsisCount(binding.descriptionTv.lineCount - 1) <= 0)
                        return@setOnClickListener
                    val dialog = AlertDialog.Builder(context)
                    .setMessage(binding.descriptionTv.text.toString())
                    .create()

                    dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_corners_shape)
                    dialog.show()
                }
            }


    }

    class ToFriendPlaylistDetailsMapper @Inject constructor(): Mapper<PlaylistUi> {
        override fun map(
            playlistId: String,
            title: String,
            is_following: Boolean,
            count: Int,
            description: String,
            owner_id: Int,
            thumbStates: List<PlaylistThumbsState>,
        ): PlaylistUi {
            return PlaylistUi(
                playlistId = playlistId.dropLast(9), //last 9 is owner id, this is need only to avoid collision in db,
                title = title,
                isFollowing = true,
                count = count,
                description = description,
                ownerId = owner_id,
                thumbStates = thumbStates
            )
        }
    }
}
