package com.kamancho.melisma.favorites.testcore

import android.graphics.Bitmap
import com.kamancho.melisma.app.core.ManagerResource

/**
 * Created by HP on 28.04.2023.
 **/
class TestManagerResource: ManagerResource {
        var valueString = ""
        var valueColor = 0

        override fun getString(id: Int): String = valueString
        override fun getColor(id: Int): Int = valueColor
        override fun getBitmap(id: Int): Bitmap {
        TODO("Not yet implemented")
        }

}