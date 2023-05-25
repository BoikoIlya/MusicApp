package com.example.musicapp.app.core

import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import com.example.musicapp.favorites.presentation.CollectTracks
import com.example.musicapp.trending.presentation.CollectUiState
import kotlinx.coroutines.flow.FlowCollector

/**
 * Created by HP on 23.05.2023.
 **/
interface CollectTracksAndUiState<T>: CollectUiState<T>, CollectTracks