package com.kamancho.melisma.editplaylist.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 19.07.2023.
 **/
interface TitleStateCommunication : Communication.Mutable<TitleUiState> {

    class Base @Inject constructor(): TitleStateCommunication,Communication.UiUpdate<TitleUiState>(TitleUiState.Success)
}