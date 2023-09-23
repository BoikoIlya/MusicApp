package com.kamancho.melisma.vkauth.di

import com.kamancho.melisma.vkauth.presentation.AuthActivity
import com.kamancho.melisma.vkauth.presentation.AuthFragment
import com.kamancho.melisma.vkauth.presentation.AuthWebViewFragment
import dagger.Subcomponent

/**
 * Created by HP on 28.01.2023.
 **/
@AuthScope
@Subcomponent(modules = [AuthModule::class,AuthProvidesModule::class])
interface AuthComponent {

    @Subcomponent.Builder
    interface Builder{
       fun build(): AuthComponent
    }

    fun inject(authFragment: AuthFragment)
    fun inject(authWebViewFragment: AuthWebViewFragment)
    fun inject(authActivity: AuthActivity)

}