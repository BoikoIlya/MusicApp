package com.kamancho.melisma.captcha.data

/**
 * Created by HP on 27.07.2023.
 **/

interface RepeatActionAfterCaptcha{

    suspend fun invokeAction()

    object Empty: RepeatActionAfterCaptcha {
        override suspend fun invokeAction() = Unit
    }
}