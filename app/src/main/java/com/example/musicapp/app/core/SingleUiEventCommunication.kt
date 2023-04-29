package com.example.musicapp.app.core

import javax.inject.Inject

/**
 * Created by HP on 08.04.2023.
 **/
interface SingleUiEventCommunication: Communication.MutableSingle<SingleUiEventState> {

    class Base @Inject constructor(): SingleUiEventCommunication, Communication.SingleUiUpdate<SingleUiEventState>()
}