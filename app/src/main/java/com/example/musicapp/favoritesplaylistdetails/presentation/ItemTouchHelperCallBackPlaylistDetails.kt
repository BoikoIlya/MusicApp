package com.example.musicapp.favoritesplaylistdetails.presentation

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.app.core.DeleteItemDialog
import com.example.musicapp.trending.presentation.MediaItemsAdapter
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import javax.inject.Inject

/**
 * Created by HP on 10.04.2023.
 **/
class ItemTouchHelperCallBackPlaylistDetails @Inject constructor(
    private val adapter: MediaItemsAdapter,
    private val viewModel: DeleteItemDialog,
    private val context: Context,
) : ItemTouchHelper.SimpleCallback(
0,
 ItemTouchHelper.RIGHT
){
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.removeFromAdapter(viewModel,viewHolder.absoluteAdapterPosition - 1)
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
}
