package com.kamancho.melisma.app.views

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import com.kamancho.melisma.app.core.ScrollUpRecyclerButton.Companion.scroll_dist
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Created by HP on 06.07.2023.
 **/

interface ScrollUpNestedScrollButtonActions{

    fun setupWithNestedScroll(scroll: NestedScrollView)


}

abstract class AbstractScrollUpButton: FloatingActionButton{

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    companion object{
        private const val key = "super_state"
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putBoolean(this.id.toString(),isShown)
        return super.onSaveInstanceState()?.let { savedState ->
             bundle.putParcelable(key, savedState)
             bundle
         }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        (state as? Bundle)?.let { bundle ->
            this.isVisible = bundle.getBoolean(this.id.toString())
            super.onRestoreInstanceState(bundle.getParcelable(key))
        }
    }
}

class ScrollUpNestedScrollButton: AbstractScrollUpButton, ScrollUpNestedScrollButtonActions {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun setupWithNestedScroll(scroll: NestedScrollView) {
            scroll.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                if(oldScrollY==0) return@setOnScrollChangeListener

                if (((scrollY- oldScrollY) > scroll_dist &&  isShown) ) hide()


                if (((scrollY-oldScrollY) < -scroll_dist && !isShown)) show()


                if (!scroll.canScrollVertically(-1)) hide()
            }

    }




}

