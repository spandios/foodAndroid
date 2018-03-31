package com.example.fooddeuk.inject.component

import com.example.fooddeuk.activity.MainActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector



/**
 * Created by heo on 2018. 3. 31..
 */


@Subcomponent(modules = arrayOf(MainModule::class))
interface MainComponent : AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>()
}