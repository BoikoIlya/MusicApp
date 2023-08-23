package com.kamancho.melisma.popular.di


import com.kamancho.melisma.popular.presentation.PopularFragment
import dagger.Subcomponent

/**
 * Created by HP on 01.05.2023.
 **/
@PopularScope
@Subcomponent(modules = [PopularModule::class,PopularModuleProvides::class])
interface PopularComponent {

    @Subcomponent.Builder
    interface Builder{
        fun build(): PopularComponent
    }

    fun inject(popularFragment: PopularFragment)

}