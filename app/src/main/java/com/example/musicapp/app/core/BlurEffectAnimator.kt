package com.example.musicapp.app.core

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import com.example.musicapp.R
import com.example.musicapp.player.presentation.getBitmapFromView
import jp.wasabeef.blurry.Blurry

/**
 * Created by HP on 21.07.2023.
 **/
interface BlurEffectAnimator {

    fun show(targetView: View)

    fun hide(targetView: View)

    @RequiresApi(Build.VERSION_CODES.S)
    data class Api31AndAbove(
        private val valueNotVisible: Float = 1f,
        private val valueVisible: Float = 20f,
        private val duration: Long = 200
    ): BlurEffectAnimator {


        override fun show(targetView: View) {
            val animator = ValueAnimator.ofFloat(valueNotVisible, valueNotVisible)
            animator.duration = duration

            animator.addUpdateListener { animation ->
                val fraction = animation.animatedFraction
                val interpolatedX: Float = valueNotVisible + fraction * (valueVisible - valueNotVisible)
                val interpolatedY: Float = valueNotVisible + fraction * (valueVisible - valueNotVisible)

                val updatedBlurEffect =
                    RenderEffect.createBlurEffect(
                        interpolatedX,
                        interpolatedY,
                        Shader.TileMode.CLAMP
                    )
               targetView.setRenderEffect(updatedBlurEffect)
            }

            animator.start()
        }

        override fun hide(targetView: View) {
            val animator = ValueAnimator.ofFloat(valueVisible, valueVisible)
            animator.duration = duration

            animator.addUpdateListener { animation ->
                val fraction = animation.animatedFraction
                val interpolatedX: Float = valueVisible + fraction * (valueNotVisible - valueVisible)
                val interpolatedY: Float = valueVisible + fraction * (valueNotVisible - valueVisible)

                val updatedBlurEffect =
                    RenderEffect.createBlurEffect(
                        interpolatedX,
                        interpolatedY,
                        Shader.TileMode.CLAMP
                    )
                targetView.setRenderEffect(updatedBlurEffect)
                if (fraction == 1f) targetView.setRenderEffect(null)
            }

            animator.start()
        }

    }


   data class BelowApi31(
       private val radius: Int = 20,
       private val duration: Int = 200,
   ): BlurEffectAnimator {

        override fun show(targetView: View) {
            Blurry.with(targetView.context)
                .radius(radius)
                .async()
                .animate(duration)
                .onto(targetView as ViewGroup)
        }

        override fun hide(targetView: View) {
            val view: View = targetView.findViewWithTag(Blurry::class.java.simpleName)
            view.animate()
                .alpha(0f)
                .setDuration(duration.toLong())
                .withEndAction {
                    (targetView as ViewGroup).removeView(view)
                }
                .start()
        }
    }
}