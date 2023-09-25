package com.kamancho.melisma.trending.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kamancho.melisma.databinding.DefaultLoadStateBinding
import com.kamancho.melisma.search.presentation.OnRetry

/**
 * Created by HP on 24.09.2023.
 **/
class PagingLoadStateAdapter(
    private val refresh:()-> Unit,
): RecyclerView.Adapter<PagingLoadStateViewHolder>() {

    private var message = emptyList<String>().toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingLoadStateViewHolder {
      return PagingLoadStateViewHolder(
          DefaultLoadStateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ), refresh)
    }

    override fun getItemCount(): Int = message.size

    override fun onBindViewHolder(holder: PagingLoadStateViewHolder, position: Int) {
           holder.bind(message[position])
    }

    fun show(message: String){
        val callback = PagingLoadStateDiffUtil(this.message, listOf(message))
        val diff = DiffUtil.calculateDiff(callback)
        this.message.clear()
        this.message.add(message)
        diff.dispatchUpdatesTo(this)
    }

    fun hide(){

        if(this.message.isNotEmpty()) {
            message.clear()
            notifyItemRemoved(0)
        }
    }

    fun loadPage() {
        if(message.isEmpty()) refresh.invoke()
    }

}

class PagingLoadStateDiffUtil(
    private val oldList:List<String>,
    private val newList: List<String>,
): DiffUtil.Callback(){

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]==newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]==newList[newItemPosition]
    }

}

class PagingLoadStateViewHolder(
    private val binding: DefaultLoadStateBinding,
    private val refresh:()-> Unit,
): RecyclerView.ViewHolder(binding.root){

    init {
        binding.loadStateErrorBtn.setOnClickListener {  refresh.invoke() }
    }

    fun bind( errorMessage:String) = with(binding){
        loadStateErrorTv.text = errorMessage
        loadStateErrorTv.isVisible = errorMessage.isNotEmpty()
        loadStateErrorBtn.isVisible = errorMessage.isNotEmpty()
        loadStateProgress.isVisible = errorMessage.isEmpty()
    }

}