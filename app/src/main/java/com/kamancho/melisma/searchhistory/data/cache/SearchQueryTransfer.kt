package com.kamancho.melisma.searchhistory.data.cache

import com.kamancho.melisma.app.core.DataTransfer
import javax.inject.Inject

/**
 * Created by HP on 04.05.2023.
 **/
interface SearchQueryTransfer: DataTransfer<Pair<String,Int>>  {

    class Base @Inject constructor(): SearchQueryTransfer,DataTransfer.Abstract<Pair<String,Int>>()
}