package com.kamancho.melisma.friends.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.ClickListener
import com.kamancho.melisma.app.core.FavoritesFragment
import com.kamancho.melisma.app.core.ImageLoader
import com.kamancho.melisma.friends.di.FriendsComponent
import com.kamancho.melisma.main.di.App
import javax.inject.Inject

/**
 * Created by HP on 18.08.2023.
 **/
class FriendsFragment: FavoritesFragment<FriendUi>(R.layout.favorites_fragment) {

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var component: FriendsComponent

    override fun onAttach(context: Context) {
        component = (context.applicationContext as App).appComponent.friendsComponent().build()
        component.inject(this)
        favoritesViewModel = ViewModelProvider(this, factory)[FriendsViewModel::class.java]
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.shuffleFavorites.visibility = View.GONE
        binding.menu.visibility = View.GONE
        binding.titleFavorites.setText(R.string.friends_title)
        binding.errorMessage.setText(R.string.no_friends)

        binding.favoritesRcv.layoutManager = LinearLayoutManager(requireContext())

        val friendsAdapter = FriendsAdapter(
            imageLoader = imageLoader,
            diskCacheStrategy = DiskCacheStrategy.AUTOMATIC,
            clickListener = object : ClickListener<Pair<String,String>>{
                override fun onClick(data: Pair<String,String>) {
                    val bundle = Bundle()
                    bundle.putString(friend_id_key,data.first)
                    bundle.putString(friend_name_key,data.second)

                    findNavController().navigate(R.id.action_friendsFragment_to_friendDetailsFragment,bundle)
                }
            },
            layoutManager = binding.favoritesRcv.layoutManager as LayoutManager
        )
        adapter = friendsAdapter
        binding.favoritesRcv.adapter = friendsAdapter

        super.onViewCreated(view, savedInstanceState)
    }

    override fun additionalActionsOnRecyclerUpdate(data: List<FriendUi>) = Unit

    override fun search(query: String) {
        (favoritesViewModel as FriendsViewModel).search(query)
    }

    companion object{
        private const val friend_id_key = "friendId"
        private const val friend_name_key = "friendName"
    }
}