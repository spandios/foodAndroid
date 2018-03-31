package com.example.fooddeuk.inject.component

import com.example.fooddeuk.`object`.GlobalApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton



/**
 * Created by heo on 2018. 3. 31..
 */
@Singleton
@Component(modules = arrayOf(AndroidInjectionModule::class,AppModule::class))
interface AppComponent {
    fun inject(daggerSampleApp: GlobalApplication)
}