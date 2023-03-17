package com.example.musicapp.main.presentation

import com.example.musicapp.trending.presentation.Select

/**
 * Created by HP on 01.02.2023.
 **/
sealed interface ItemPositionState{

    fun apply(adapter: Select)

    fun apply(block:(Int)->Unit)


   open class SetPosition(private val position: Int): ItemPositionState {

       override fun apply(adapter: Select) {
        //   adapter.newPosition(position)
       }
       override fun apply(block: (Int) -> Unit) = block(position)


   }

    data class UpdateRecyclerViewSelectedItem(private val position: Int):  SetPosition(position){

        override fun apply(adapter: Select) = adapter.updateSelectedItem(position)

    }
}