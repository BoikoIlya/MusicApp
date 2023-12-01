package com.kamancho.melisma.captcha.data

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 27.07.2023.
 **/
interface CaptchaCommunication: Communication.Mutable<Pair<String,RepeatActionAfterCaptcha>> {

    class Base @Inject constructor(): CaptchaCommunication,
        Communication.UiUpdate<Pair<String,RepeatActionAfterCaptcha>>(Pair("",RepeatActionAfterCaptcha.Empty))
}