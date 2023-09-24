package com.kamancho.melisma.vkauth.presentation

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.captcha.data.CaptchaRepository
import com.kamancho.melisma.captcha.presentation.CaptchaFragmentDialog
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 24.09.2023.
 **/
class MainAuthViewModel @Inject constructor(
    private val dispatchersList: DispatchersList,
    private val singleAuthCommunication: SingleAuthCommunication,
    private val captchaRepository: CaptchaRepository,
): ViewModel() {

    init {
        handleCaptcha()
    }

    fun handleCaptcha() = viewModelScope.launch(dispatchersList.io()) {
        captchaRepository.collectCaptchaData{
            if(it.first.isNotEmpty())
                singleAuthCommunication.map(SingleAuthState.ShowDialog(CaptchaFragmentDialog()))
        }
    }

    suspend fun collectSingleAuthCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<SingleAuthState>
    ) = singleAuthCommunication.collect(owner, collector)
}