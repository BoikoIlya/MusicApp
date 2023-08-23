package com.example.musicapp.app.core

import com.example.musicapp.captcha.data.CaptchaRepository
import com.example.musicapp.captcha.data.RepeatActionAfterCaptcha

/**
 * Created by HP on 25.06.2023.
 **/
class DuplicateException: Exception()

class UnAuthorizedException: Exception()

class CaptchaNeededException(
    private val id: String,
    private val url: String
): Exception(),Mapper<Pair<CaptchaRepository,RepeatActionAfterCaptcha>,Unit> {

    override fun map(data: Pair<CaptchaRepository, RepeatActionAfterCaptcha>) {
        data.first.saveNewCaptchaData(id,url,data.second)
    }
}
