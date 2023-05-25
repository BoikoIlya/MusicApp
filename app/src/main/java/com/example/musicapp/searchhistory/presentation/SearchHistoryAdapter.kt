package com.example.musicapp.searchhistory.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.media3.common.MediaItem
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.musicapp.app.core.*
import com.example.musicapp.databinding.HistoryItemBinding


/**
 * Created by HP on 30.01.2023.
 **/

class SearchHistoryAdapter(
    private val clickListener: ClickListener<String>,
    private val selector: Selector<String>
): RecyclerView.Adapter<SearchHistoryViewHolder>(),
    Mapper<List<String>, Unit>{

    private val historyList = mutableListOf<String>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        return SearchHistoryViewHolder(
            HistoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            clickListener,selector
        )
    }

    override fun getItemCount(): Int = historyList.size

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
            holder.bind(historyList[position])
    }

    override fun map(data: List<String>) {
            val diff = SearchHistoryDiffUtilCallback(data, historyList)
            val result = DiffUtil.calculateDiff(diff)
            historyList.clear()
            historyList.addAll(data)
            result.dispatchUpdatesTo(this@SearchHistoryAdapter)
    }

}

class SearchHistoryViewHolder(
    private val binding: HistoryItemBinding,
    private val clickListener: ClickListener<String>,
    private val selector: Selector<String>
): ViewHolder(binding.root){

    fun bind(item: String) {
        binding.historyItemTitle.text = item
        binding.root.setOnClickListener {
            selector.onSelect(item,0)
        }
        binding.removeHistoryBtn.setOnClickListener {
            clickListener.onClick(item)
        }
    }

}

class SearchHistoryDiffUtilCallback(
    private val newList: List<String>,
    private val oldList: List<String>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition].hashCode() == oldList[oldItemPosition].hashCode()
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return  newList[newItemPosition] == oldList[oldItemPosition]

    }

}