package com.kamancho.melisma.captcha.data

import android.util.Log
import com.kamancho.melisma.captcha.data.cache.CaptchaDataStore
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

/**
 * Created by HP on 27.07.2023.
 **/
interface CaptchaRepository {

    fun saveNewCaptchaData(id: String,url: String,actionCausedCaptcha: RepeatActionAfterCaptcha)

    fun saveEnteredDataFromCaptcha(data: String)

    fun clearCaptcha()

    suspend fun collectCaptchaData(
        collector: FlowCollector<Pair<String, RepeatActionAfterCaptcha>>,
    )

    class Base @Inject constructor(
        private val captchaDataSource: CaptchaDataStore,
        private val communication: CaptchaCommunication
    ): CaptchaRepository {


        override fun saveNewCaptchaData(id: String,url: String,actionCausedCaptcha: RepeatActionAfterCaptcha) {
                captchaDataSource.saveNewCaptchaData(id)
                communication.map(Pair(url, actionCausedCaptcha))
        }

        override fun saveEnteredDataFromCaptcha(data: String) {
            captchaDataSource.saveEnteredDataFromCaptcha(data)
        }

        override fun clearCaptcha() {
            captchaDataSource.saveNewCaptchaData("")
            captchaDataSource.saveEnteredDataFromCaptcha("")
        }

        override suspend fun collectCaptchaData(
            collector: FlowCollector<Pair<String, RepeatActionAfterCaptcha>>,
        ) = communication.collectIgnoreLifecycle(collector)
    }
}



