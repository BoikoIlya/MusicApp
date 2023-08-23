package com.kamancho.melisma.app.core

import android.view.View
import android.view.ViewGroup
import jp.wasabeef.blurry.Blurry

/**
 * Created by HP on 21.07.2023.
 **/
interface BlurEffectAnimator {

    fun show(targetView: View)

    fun hide(targetView: View)

    class Base(
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