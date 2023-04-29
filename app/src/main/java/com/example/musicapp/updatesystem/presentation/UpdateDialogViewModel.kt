package com.example.musicapp.updatesystem.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.UiEventState
import com.example.musicapp.main.presentation.UiEventsCommunication
import com.example.musicapp.updatesystem.data.UpdateDialogMapper
import com.example.musicapp.updatesystem.data.UpdateSystemRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 23.04.2023.
 **/
class UpdateDialogViewModel @Inject constructor(
    private val updateSystemRepository: UpdateSystemRepository,
    private val dateTransfer: DataTransfer.UpdateDialogTransfer,
    private val uiEventsCommunication: UiEventsCommunication,
    private val dispatchersList: DispatchersList,
    private val mapper: UpdateDialogMapper,
): ViewModel() {

    fun readDialogMessage() = dateTransfer.read()

    fun loadUpdate() = viewModelScope.launch(dispatchersList.io()) {
        dismiss()
        updateSystemRepository.retriveApkUrl().map(mapper)
    }

    fun dismiss(){
        uiEventsCommunication.map(UiEventState.ClearCommunication)
    }

}