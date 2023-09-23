package com.kamancho.melisma.app.core

import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by HP on 04.07.2023.
 **/
interface FormatTimeSecondsToMinutesAndSeconds {

    fun format(timeSeconds: Int): String

    class Base @Inject constructor(): FormatTimeSecondsToMinutesAndSeconds{

        override fun format(timeSeconds: Int):String {
            val minutes: Long = TimeUnit.SECONDS.toMinutes(timeSeconds.toLong())
            val seconds: Long = timeSeconds - TimeUnit.MINUTES.toSeconds(minutes)
            return String.format("%02d:%02d", minutes, seconds)
        }

    }
}