package com.example.musicapp.captcha.data.cache

import javax.inject.Inject

/**
 * Created by HP on 27.07.2023.
 **/
interface CaptchaDataStore{

    fun saveNewCaptchaData(id: String)

    fun saveEnteredDataFromCaptcha(data: String)

    fun captchaId():String

    fun captchaEnteredData(): String

    class Base @Inject constructor(): CaptchaDataStore {
        private var id: String =""
        private var enteredData: String =""

        override fun saveNewCaptchaData(id: String) {
            this.id = id
        }

        override fun saveEnteredDataFromCaptcha(data: String) {
            this.enteredData = data
        }

        override fun captchaId(): String = id

        override fun captchaEnteredData(): String = enteredData

    }
}