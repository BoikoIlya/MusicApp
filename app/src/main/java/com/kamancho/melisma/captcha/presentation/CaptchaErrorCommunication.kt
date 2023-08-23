package com.kamancho.melisma.captcha.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 27.07.2023.
 **/
interface CaptchaErrorCommunication: Communication.Mutable<String> {

    class Base @Inject constructor(): CaptchaErrorCommunication, Communication.UiUpdate<String>("")
}