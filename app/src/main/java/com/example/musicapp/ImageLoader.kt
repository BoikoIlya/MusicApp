package com.example.musicapp

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import javax.inject.Inject

/**
 * Created by HP on 30.01.2023.
 **/
interface ImageLoader {

    fun loadImage(
        url:String,
        target: ImageView,
        placeholder: Int
    )



    class Base @Inject constructor(): ImageLoader{

        override fun loadImage(url: String, target: ImageView, placeholder: Int) {
            Glide.with(target.context).load(url).placeholder(placeholder).into(target)
        }

    }
}