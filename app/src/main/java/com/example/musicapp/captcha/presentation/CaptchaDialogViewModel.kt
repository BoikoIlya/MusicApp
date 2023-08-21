package com.example.musicapp.captcha.presentation

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.R
import com.example.musicapp.captcha.data.CaptchaRepository
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.captcha.data.RepeatActionAfterCaptcha
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 27.07.2023.
 **/
class CaptchaDialogViewModel @Inject constructor(
    private val repository: CaptchaRepository,
    private val dispatchersList: DispatchersList,
    private val captchaErrorCommunication: CaptchaErrorCommunication,
    private val managerResource: ManagerResource,
    private val dismissDialogCommunication: DismissDialogCommunication
): ViewModel() {

    fun submit(enteredData: String, action: RepeatActionAfterCaptcha) = viewModelScope.launch(dispatchersList.io()) {
        if(enteredData.isBlank()){
            captchaErrorCommunication.map(managerResource.getString(R.string.dont_live_empty))
            return@launch
        }
        dismissDialogCommunication.map(Unit)
        captchaErrorCommunication.map("")
        repository.saveEnteredDataFromCaptcha(enteredData.trim())
        action.invokeAction()
    }

    fun setError(errorMessageId: Int) = captchaErrorCommunication.map(managerResource.getString(errorMessageId))

    suspend fun collectCaptchaData(
        collector: FlowCollector<Pair<String, RepeatActionAfterCaptcha>>
    ) = repository.collectCaptchaData(collector)

    suspend fun collectCaptchaError(
        owner: LifecycleOwner,
        collector: FlowCollector<String>
    ) = captchaErrorCommunication.collect(owner, collector)

    suspend fun collectDismissDialog(
        owner: LifecycleOwner,
        collector: FlowCollector<Unit>
    ) = dismissDialogCommunication.collect(owner, collector)
}