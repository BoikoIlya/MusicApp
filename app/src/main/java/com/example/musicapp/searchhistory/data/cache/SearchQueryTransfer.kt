package com.example.musicapp.searchhistory.data.cache

import com.example.musicapp.app.core.DataTransfer
import javax.inject.Inject

/**
 * Created by HP on 04.05.2023.
 **/
interface SearchQueryTransfer: DataTransfer<String>  {

    class Base @Inject constructor(): SearchQueryTransfer,DataTransfer.Abstract<String>()
}