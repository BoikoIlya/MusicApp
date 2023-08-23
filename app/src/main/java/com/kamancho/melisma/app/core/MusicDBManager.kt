package com.kamancho.melisma.app.core

import javax.inject.Inject

/**
 * Created by HP on 28.07.2023.
 **/
interface MusicDBManager {

    suspend fun clearAllTables()

    class Base @Inject constructor(
        private val db: MusicDatabase
    ): MusicDBManager {

        override suspend fun clearAllTables() = db.clearAllTables()

    }

}