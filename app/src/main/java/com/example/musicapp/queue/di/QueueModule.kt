package com.example.musicapp.queue.di

import androidx.lifecycle.ViewModel
import com.example.musicapp.main.di.ViewModelKey
import com.example.musicapp.queue.presenatation.QueueViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by HP on 23.04.2023.
 **/
@Module
interface QueueModule {
    @Binds
    @[IntoMap ViewModelKey(QueueViewModel::class)]
    fun bindQueueViewModel(queueViewModel: QueueViewModel): ViewModel
}