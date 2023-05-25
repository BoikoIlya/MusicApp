package com.example.musicapp.updatesystem.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.R
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.SingleUiEventCommunication
import com.example.musicapp.app.core.SingleUiEventState
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 23.04.2023.
 **/
class UpdateDialogViewModel @Inject constructor(
    private val dateTransfer: DataTransfer.UpdateDialogTransfer,
    private val singleUiEventCommunication: SingleUiEventCommunication,
    private val dispatchersList: DispatchersList,
    private val managerResource: ManagerResource,
): ViewModel() {

    fun readDialogMessageAndUrl() = dateTransfer.read()

    fun loadUpdate(url: String) = viewModelScope.launch(dispatchersList.io()) {
        if (url.isNotEmpty()) {
            singleUiEventCommunication.map(SingleUiEventState.LoadUpdate(url))
        } else {
            singleUiEventCommunication.map(
                SingleUiEventState.ShowSnackBar.Error(
                    managerResource.getString(R.string.empty_url_error)
                )
            )
            singleUiEventCommunication.map(SingleUiEventState.ShowDialog(UpdateDialogFragment()))
        }
    }


}