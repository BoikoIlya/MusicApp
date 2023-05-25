package com.example.musicapp.favorites.testcore

import com.example.musicapp.app.core.ManagerResource

/**
 * Created by HP on 28.04.2023.
 **/
class TestManagerResource: ManagerResource {
        var valueString = ""
        var valueColor = 0

        override fun getString(id: Int): String = valueString
        override fun getColor(id: Int): Int = valueColor

    }