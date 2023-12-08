package com.kamancho.melisma.favorites.presentation

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.DeleteItemDialog
import com.kamancho.melisma.favoritesplaylistdetails.presentation.PlaylistTopBarViewHolder
import com.kamancho.melisma.trending.presentation.MediaItemsAdapter
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import javax.inject.Inject

/**
 * Created by HP on 10.04.2023.
 **/
class ItemTouchHelperCallBackFavorites @Inject constructor(
    private val adapter: MediaItemsAdapter,
    private val viewModel: DeleteItemDialog,
    private val context: Context,
) : ItemTouchHelper.SimpleCallback(
0,
ItemTouchHelper.RIGHT + ItemTouchHelper.LEFT
){
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if(direction == ItemTouchHelper.LEFT)
            adapter.onLeftSwipe(MediaItem.EMPTY, viewHolder.absoluteAdapterPosition)
        else
            adapter.onRightSwipe(viewModel,viewHolder.absoluteAdapterPosition)
    }


    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        if (dX < 0) {

            RecyclerViewSwipeDecorator.Builder(
                c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
                .addBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.green
                    )
                )
                .addActionIcon(R.drawable.music_folder1)
                .create()
                .decorate()

        }else {

            RecyclerViewSwipeDecorator.Builder(
                c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
                .addBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.red
                    )
                )
                .addActionIcon(R.drawable.bin)
                .create()
                .decorate()
        }

        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        if(viewHolder is PlaylistTopBarViewHolder) return 0
        return super.getSwipeDirs(recyclerView, viewHolder)
    }
}
