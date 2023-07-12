package com.example.musicapp.app.core

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Created by HP on 06.07.2023.
 **/
class ScrollUpButton: FloatingActionButton, ScrollUpButtonActions {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    companion object{
        private const val scroll_dist = 10
        private const val item_position_to_scroll_not_smooth = 20
    }

    override fun setupWithRecycler(rcv: RecyclerView) {
        rcv.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > scroll_dist &&  isShown) hide()


                if (dy < -scroll_dist && !isShown) show()


                if (!recyclerView.canScrollVertically(-1)) hide()
            }}
        )

        val layoutManager = (rcv.layoutManager as LinearLayoutManager)

        setOnClickListener{
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            if((rcv.adapter?.itemCount ?: item_position_to_scroll_not_smooth ) >= item_position_to_scroll_not_smooth
                && firstVisibleItemPosition > item_position_to_scroll_not_smooth)
                rcv.scrollToPosition(item_position_to_scroll_not_smooth)

            rcv.smoothScrollToPosition(0)
        }
    }


}

interface ScrollUpButtonActions{

    fun setupWithRecycler(rcv: RecyclerView)

}