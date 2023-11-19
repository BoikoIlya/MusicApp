package com.kamancho.melisma.trending.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by Ilya Boiko @camancho
on 09.11.2023.
 **/

interface EmbeddedVkPlaylistsCommunication : Communication.Mutable<List<TrendingTopBarItemUi>> {
    class Base @Inject constructor() :
        Communication.UiUpdate<List<TrendingTopBarItemUi>>(emptyList()),
        EmbeddedVkPlaylistsCommunication
}