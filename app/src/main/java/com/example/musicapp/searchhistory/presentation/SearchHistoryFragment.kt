package com.example.musicapp.searchhistory.presentation

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.musicapp.R

/**
 * Created by HP on 01.05.2023.
 **/
class SearchHistoryFragment: Fragment(R.layout.search_history_fragment) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val edittext = requireActivity().findViewById<EditText>(R.id.search_history_edt)

        edittext.setOnClickListener {
            findNavController().navigate(R.id.action_searchHistoryFragment_to_searchFragment)
        }
    }
}