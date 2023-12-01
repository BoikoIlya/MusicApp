package com.kamancho.melisma.trending.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kamancho.melisma.app.core.ClickListener
import com.kamancho.melisma.databinding.ErrorLayoutBinding

/**
 * Created by HP on 07.08.2023.
 **/
class ErrorLayoutAdapter(
    private val clickListener: ClickListener<Unit>,
): RecyclerView.Adapter<ErrorLayoutViewHolder>() {

    private var message = ""


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ErrorLayoutViewHolder {
        return ErrorLayoutViewHolder(
            ErrorLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),clickListener)
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: ErrorLayoutViewHolder, position: Int) = holder.bind(message)

    fun setup(message: String){
        this.message = message
        notifyItemInserted(0)
    }

    fun hide(){
        notifyItemRemoved(0)
    }
}

class ErrorLayoutViewHolder(
    private val binding: ErrorLayoutBinding,
    private val clickListener: ClickListener<Unit>
): ViewHolder(binding.root){

    fun bind(message: String,){
        binding.errorTv.text = message
        binding.reloadBtn.setOnClickListener { clickListener.onClick(Unit) }
    }
}