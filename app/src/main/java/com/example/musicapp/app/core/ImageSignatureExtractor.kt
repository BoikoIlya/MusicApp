package com.example.musicapp.app.core

import javax.inject.Inject

/**
 * Created by HP on 13.08.2023.
 **/
interface ImageSignatureExtractor {

    fun extract(url: String): String

    class Base @Inject constructor(): ImageSignatureExtractor {

        override fun extract(url: String): String {
            val regex = Regex("sign=([^&]+)")
            val matchResult = regex.find(url)
            return matchResult?.groupValues?.getOrNull(1)?:""
        }
    }
}