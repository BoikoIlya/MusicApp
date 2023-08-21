package com.example.musicapp.favorites.testcore

import com.example.musicapp.app.core.SearchQueryRepository

/**
 * Created by HP on 21.05.2023.
 **/
abstract class TestSearchQueryRepository: SearchQueryRepository {
    var query = ""

    override fun readQueryAndHistoryType(): String = query

    override suspend fun saveQueryInDB(query: String) {
       this.query = query
    }
}