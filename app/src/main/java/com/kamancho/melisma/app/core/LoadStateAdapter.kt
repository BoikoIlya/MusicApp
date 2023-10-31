package com.kamancho.melisma.app.core

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kamancho.melisma.databinding.DefaultLoadStateBinding

/**
 * Created by Ilya Boiko @camancho
on 26.10.2023.
 **/
class LoadStateAdapter(
 private val onRefresh: () -> Unit,
) : RecyclerView.Adapter<LoadStateViewHolder>() {

    private val message = emptyList<String>().toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadStateViewHolder {
        return LoadStateViewHolder(
            DefaultLoadStateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onRefresh
        )
    }

    override fun getItemCount(): Int = message.size

    override fun onBindViewHolder(holder: LoadStateViewHolder, position: Int) {
        holder.bind(message[position])
    }

    fun showState(newList: List<String>) {
        val callback = LoadStateDiffUtillCallback(message, newList)
        val diff = DiffUtil.calculateDiff(callback)
        message.clear()
        message.addAll(newList)
        diff.dispatchUpdatesTo(this)
    }


}

class LoadStateViewHolder(
 private val binding: DefaultLoadStateBinding,
 private val onRefresh: () -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(message: String) {
        binding.loadStateProgress.isVisible = message.isEmpty()
        binding.loadStateErrorBtn.isVisible = message.isNotEmpty()
        binding.loadStateErrorTv.isVisible = message.isNotEmpty()
        binding.loadStateErrorTv.text = message

        binding.loadStateErrorBtn.setOnClickListener { onRefresh.invoke() }
    }

}


class LoadStateDiffUtillCallback(
    private val oldList: List<String>,
    private val newList: List<String>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = false


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == oldList[newItemPosition]

}