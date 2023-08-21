package com.example.musicapp.hlscachesystem.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 09.08.2023.
 **/
interface HLSCachingResultCommunication: Communication.Mutable<HLSCachingResult> {

    class Base @Inject constructor(): HLSCachingResultCommunication, Communication.UiUpdate<HLSCachingResult>(HLSCachingResult.Empty)
}