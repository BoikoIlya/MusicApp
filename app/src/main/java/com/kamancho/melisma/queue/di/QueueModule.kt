package com.kamancho.melisma.queue.di

import androidx.lifecycle.ViewModel
import com.kamancho.melisma.main.di.ViewModelKey
import com.kamancho.melisma.queue.presenatation.QueueViewModel
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