package com.example.musicapp.vkauth.di

import com.example.musicapp.vkauth.presentation.AuthActivity
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