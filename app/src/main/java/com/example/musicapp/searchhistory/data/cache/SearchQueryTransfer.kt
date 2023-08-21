package com.example.musicapp.searchhistory.data.cache

import android.util.Log
import com.example.musicapp.app.core.DataTransfer
import javax.inject.Inject

/**
 * Created by HP on 04.05.2023.
 **/
interface SearchQueryTransfer: DataTransfer<Pair<String,Int>>  {

    class Base @Inject constructor(): SearchQueryTransfer,DataTransfer.Abstract<Pair<String,Int>>()
}