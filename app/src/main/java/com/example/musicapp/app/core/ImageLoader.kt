package com.example.musicapp.app.core

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.musicapp.R
import javax.inject.Inject

/**
 * Created by HP on 30.01.2023.
 **/
interface ImageLoader {

    fun loadImage(
        url:String,
        target: ImageView,
    )



    class Base @Inject constructor(): ImageLoader {

        override fun loadImage(url: String, target: ImageView) {
            Glide.with(target)
                .load(url)
                .thumbnail(Glide.with(target).asDrawable().sizeMultiplier(0.2f))
                .placeholder(R.drawable.tone)
                .into(target)
        }

    }
}