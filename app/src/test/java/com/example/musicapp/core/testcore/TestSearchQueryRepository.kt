package com.example.musicapp.core.testcore

import com.example.musicapp.app.core.SearchQueryRepository

/**
 * Created by HP on 21.05.2023.
 **/
abstract class TestSearchQueryRepository: SearchQueryRepository {
    var query = ""

    override fun readQuery(): String = query

    override suspend fun saveQuery(query: String) {
       this.query = query
    }
}