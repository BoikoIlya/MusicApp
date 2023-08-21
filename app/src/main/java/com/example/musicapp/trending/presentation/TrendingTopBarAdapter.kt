package com.example.musicapp.trending.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.app.core.Mapper
import com.example.musicapp.databinding.TrendingTopBarItemBinding

/**
 * Created by HP on 30.01.2023.
 **/

class TrendingTopBarAdapter(
    private val imageLoader: ImageLoader,
    private val navController: NavController
   // private val context: Context
): RecyclerView.Adapter<TopBarViewHolder>(), Mapper<List<TrendingTopBarItemUi>, Unit> {

    private val playlists = mutableListOf<TrendingTopBarItemUi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopBarViewHolder {

//        when(viewType){
//            R.layout.title_item -> TitleViewHolder(
//                TitleItemBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                ))
           //  else ->
        return      TopBarViewHolder(
            TrendingTopBarItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), navController, imageLoader
            )
        //}
    }

    override fun getItemCount(): Int = playlists.size

//    override fun getItemViewType(position: Int): Int {
//        return when(position){
//            0-> R.layout.title_item
//            1-> R.layout.playlist_item
//            else -> R.layout.title_item
//        }
//    }

    override fun onBindViewHolder(holder: TopBarViewHolder, position: Int) {
//        when(position){
//                0-> holder.bind(context.getString(R.string.trending_header),null)
//                1->
//                2-> holder.bind(context.getString(R.string.top_200_recommendations),null)
//        }
       holder.bind(playlists[position])
     //   holder.bind("",playlists[position])
    }

    override  fun map(data: List<TrendingTopBarItemUi>) {
        val diff = PlaylistDiffUtilCallback(data, playlists)
        val result = DiffUtil.calculateDiff(diff)
        playlists.clear()
        playlists.addAll(data)
        result.dispatchUpdatesTo(this)
    }


}

abstract class TrendingViewHolderAbstract(view: View):ViewHolder(view){

   abstract fun bind(message: String,playlistUi: TrendingTopBarItemUi?)

}

class TopBarViewHolder(
    private val binding: TrendingTopBarItemBinding,
    private val navController: NavController,
    private val imageLoader: ImageLoader
):ViewHolder(binding.root){

    private val mapper = TrendingTopBarItemUi.ListItemUi(binding, imageLoader, navController)

    fun bind(playlistUi: TrendingTopBarItemUi){
        playlistUi.map(mapper)
    }

//    override fun bind(message: String, playlistUi: PlaylistUi?) {
//        Log.d("tag", "bind: playlist")
//        playlistUi?.map(mapper)
//    }
}

class PlaylistDiffUtilCallback(
    private val newList: List<TrendingTopBarItemUi>,
    private val oldList: List<TrendingTopBarItemUi>
): DiffUtil.Callback(){

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition].map(oldList[oldItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition] == oldList[oldItemPosition]
    }

}