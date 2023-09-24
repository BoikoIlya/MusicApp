package com.kamancho.melisma.app.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kamancho.melisma.R


/**
 * Created by HP on 30.01.2023.
 **/
interface ImageLoader {

    fun loadImage(
        url:String,
        targetView: ImageView,
        placeholderId: Int = R.drawable.tone_yellow,
        cacheStrategy: DiskCacheStrategy,
    )

    fun loadImage(
        bigImageUrl:String,
        targetView: ImageView,
        imgBg: CardView,
        smallImageUrl: String
    )

    fun loadImageForPlaylist(
        url:String,
        targetView: ImageView,
        cacheStrategy: DiskCacheStrategy
    )

    fun loadCaptcha(
        url:String,
        targetView: ImageView,
        error: ()->Unit,
        success: ()->Unit,
    )

   suspend fun clearData()

     class Base (
         private val paintBackgroundShadow: PaintBackgroundShadow,
         private val context: Context
     ): ImageLoader {

         companion object{
             private const val duration_in_ms = 200
         }

        override fun loadImage(url: String, targetView: ImageView,placeholderId: Int,cacheStrategy: DiskCacheStrategy) {
            GlideApp.with(targetView)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade(duration_in_ms))
                .placeholder(placeholderId)
                .diskCacheStrategy(cacheStrategy)
                .into(targetView)

        }

        override fun loadImage(
            bigImageUrl: String,
            targetView: ImageView,
            imgBg: CardView,
            smallImageUrl: String
            ) {
            GlideApp.with(targetView)
                .asBitmap()
                .load(bigImageUrl)
                .transition(withCrossFade(duration_in_ms))
                .placeholder(R.drawable.tone_yellow)
                .listener(object :RequestListener<Bitmap>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        return true
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        paintBackgroundShadow.paint(imgBg,resource!!)
                        return false
                    }


                })
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(targetView)
        }

         override fun loadImageForPlaylist(url: String, targetView: ImageView,cacheStrategy: DiskCacheStrategy) {
             GlideApp.with(targetView)
                 .load(url)
                 .transition(DrawableTransitionOptions.withCrossFade(duration_in_ms))
                 .placeholder(R.drawable.tone_yellow)
                 .override(300,300)
                 .diskCacheStrategy(cacheStrategy)
                 .into(targetView)
         }

         override fun loadCaptcha(
             url: String,
             targetView: ImageView,
             error: ()->Unit,
             success: ()->Unit,
         ) {
             GlideApp.with(targetView)
                 .load(url)
                 .transition(DrawableTransitionOptions.withCrossFade(duration_in_ms))
                 .listener(object :RequestListener<Drawable>{
                     override fun onLoadFailed(
                         e: GlideException?,
                         model: Any?,
                         target: Target<Drawable>?,
                         isFirstResource: Boolean,
                     ): Boolean {
                         targetView.setImageResource(R.drawable.retry)
                         error.invoke()
                         return true
                     }

                     override fun onResourceReady(
                         resource: Drawable?,
                         model: Any?,
                         target: Target<Drawable>?,
                         dataSource: DataSource?,
                         isFirstResource: Boolean,
                     ): Boolean {
                        success.invoke()
                         return false
                     }


                 })
                 .diskCacheStrategy(DiskCacheStrategy.NONE)
                 .into(targetView)

         }

         override suspend fun clearData() {
             Glide.get(context).clearDiskCache()
         }

     }
}