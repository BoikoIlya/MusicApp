package com.kamancho.melisma.friends.presentation

import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.ClickListener
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.databinding.FriendItemBinding

/**
 * Created by HP on 17.08.2023.
 **/
data class FriendUi(
    private val id: Int,
    private val firstAndSecondName: String,
    private val photoUrl: String
){

    fun map(item: FriendUi): Boolean = item.id== this.id

    fun <T>map(mapper: Mapper<T>): T = mapper.map(id, firstAndSecondName, photoUrl)

    interface Mapper<T>{

        fun map(
            id: Int,
            firstAndSecondName: String,
            photoUrl: String
        ): T
    }

    class ShowToUiMapper(
        private val binding: FriendItemBinding,
        private val imageLoader: ImageLoader,
        private val diskCacheStrategy: DiskCacheStrategy,
        private val clickListener: ClickListener<Pair<String,String>>
    ): Mapper<Unit>{

        override fun map(
            id: Int,
            firstAndSecondName: String,
            photoUrl: String,
        ) = with(binding) {
            imageLoader.loadImage(photoUrl,friendImg, R.drawable.user,diskCacheStrategy)
            friendFirstAndSecondNameTv.text = firstAndSecondName

            root.setOnClickListener {
                clickListener.onClick(Pair(id.toString(),firstAndSecondName))
            }
        }
    }

}
