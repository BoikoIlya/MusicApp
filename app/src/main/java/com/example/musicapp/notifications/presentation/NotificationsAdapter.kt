package com.example.musicapp.notifications.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.app.core.Mapper
import com.example.musicapp.databinding.NotificationItemBinding

/**
 * Created by HP on 21.08.2023.
 **/
class NotificationsAdapter(
    private val context: Context
): RecyclerView.Adapter<NotificationUiViewHolder>(), Mapper<List<NotificationUi>, Unit> {

    private val list = emptyList<NotificationUi>().toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationUiViewHolder {
        return NotificationUiViewHolder(
            NotificationItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ),
            context
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: NotificationUiViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun map(data: List<NotificationUi>) {
        val diff = NotificationsDiffUtilCallback(list,data)
        val result = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(data)
        result.dispatchUpdatesTo(this)
    }
}

class NotificationUiViewHolder(
    private val binding: NotificationItemBinding,
    private val context: Context
): RecyclerView.ViewHolder(binding.root){

    fun bind(item: NotificationUi) = item.apply(binding,context)
}

class NotificationsDiffUtilCallback(
    private val oldList: List<NotificationUi>,
    private val newList: List<NotificationUi>
): DiffUtil.Callback(){

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition] as NotificationUi.Abstract)==(newList[newItemPosition] as NotificationUi.Abstract)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      return (oldList[oldItemPosition] as NotificationUi.Abstract)==(newList[newItemPosition] as NotificationUi.Abstract)
    }

}