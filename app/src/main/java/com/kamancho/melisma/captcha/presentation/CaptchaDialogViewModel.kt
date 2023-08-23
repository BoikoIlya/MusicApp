package com.kamancho.melisma.captcha.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamancho.melisma.R
import com.kamancho.melisma.captcha.data.CaptchaRepository
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.ManagerResource
import com.kamancho.melisma.captcha.data.RepeatActionAfterCaptcha
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