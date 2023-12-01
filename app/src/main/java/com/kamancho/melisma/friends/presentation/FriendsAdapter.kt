package com.kamancho.melisma.friends.presentation

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kamancho.melisma.app.core.ClickListener
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.app.core.Mapper
import com.kamancho.melisma.databinding.FriendItemBinding

/**
 * Created by HP on 18.08.2023.
 **/
class FriendsAdapter(
    private val imageLoader: ImageLoader,
    private val diskCacheStrategy: DiskCacheStrategy,
    private val clickListener: ClickListener<Pair<String,String>>,
    private val layoutManager: LayoutManager
): RecyclerView.Adapter<FriendsViewHolder>(), Mapper<List<FriendUi>, Unit> {

    private val diff =AsyncListDiffer<FriendUi>(this,FriendUiItemDiffUtilCallback())
    private var recyclerViewState: Parcelable? = null

    init {
        diff.addListListener { _, _ ->
            layoutManager.onRestoreInstanceState(recyclerViewState)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        return FriendsViewHolder(
           FriendItemBinding.inflate(
                LayoutInflater.from(parent.context),
                    parent,
        false),
            imageLoader, diskCacheStrategy,clickListener
        )
    }

    override fun getItemCount(): Int = diff.currentList.size

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
       holder.bind(diff.currentList[position])
    }

    override fun map(data: List<FriendUi>) {

        recyclerViewState = layoutManager.onSaveInstanceState()
        diff.submitList(data)
    }
}

class FriendsViewHolder(
    binding: FriendItemBinding,
    imageLoader: ImageLoader,
    diskCacheStrategy: DiskCacheStrategy,
    clickListener: ClickListener<Pair<String,String>>
): ViewHolder(binding.root){

    private val mapper = FriendUi.ShowToUiMapper(binding, imageLoader, diskCacheStrategy, clickListener)

    fun bind(item: FriendUi)= item.map(mapper)
}

class FriendUiItemDiffUtilCallback: DiffUtil.ItemCallback<FriendUi>(){


    override fun areItemsTheSame(oldItem: FriendUi, newItem: FriendUi): Boolean {
        return newItem.map(oldItem)
    }

    override fun areContentsTheSame(oldItem: FriendUi, newItem: FriendUi): Boolean {
        return  oldItem == newItem
    }

}