package com.kamancho.melisma.vkauth.di

import com.kamancho.melisma.vkauth.presentation.AuthActivity
import dagger.Subcomponent

/**
 * Created by HP on 28.01.2023.
 **/
@AuthScope
@Subcomponent(modules = [AuthModule::class])
interface AuthComponent {

    @Subcomponent.Builder
    interface Builder{
       fun build(): AuthComponent
    }

    fun inject(authActivity: AuthActivity)

}