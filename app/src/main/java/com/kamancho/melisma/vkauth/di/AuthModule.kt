package com.kamancho.melisma.vkauth.di

import androidx.lifecycle.ViewModel

import com.kamancho.melisma.main.di.ViewModelKey
import com.kamancho.melisma.vkauth.domain.AuthResult
import com.kamancho.melisma.vkauth.domain.AuthorizationInteractor

import com.kamancho.melisma.vkauth.presentation.AuthCommunication
import com.kamancho.melisma.vkauth.presentation.AuthResultMapper
import com.kamancho.melisma.vkauth.presentation.AuthViewModel
import com.kamancho.melisma.vkauth.presentation.SingleAuthCommunication
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.multibindings.IntoMap

/**
 * Created by HP on 28.01.2023.
 **/


@Module
interface AuthModule{

    @Reusable
    @Binds
    fun bindAuthCommunication(obj: AuthCommunication.Base): AuthCommunication

    @Binds
    @AuthScope
    fun bindAuthorizationIntercator(obj: AuthorizationInteractor.Base): AuthorizationInteractor

//    @Reusable
//    @Binds
//    fun bindRedirectionCommunication(obj: SingleAuthCommunication.Base): SingleAuthCommunication


    @Reusable
    @Binds
    fun bindAuthResultMapper(obj: AuthResultMapper): AuthResult.Mapper

    @Binds
    @[IntoMap ViewModelKey(AuthViewModel::class)]
    fun bindTrendingViewModel(authViewModel: AuthViewModel): ViewModel

}

@Module
class AuthProvidesModule{

    @Provides
    @AuthScope
    fun bindSuccessAuthCommunication(): SingleAuthCommunication{
        return SingleAuthCommunication.Base
    }
}

