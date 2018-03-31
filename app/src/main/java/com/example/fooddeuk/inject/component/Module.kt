package com.example.fooddeuk.inject.component

import android.app.Activity
import com.example.fooddeuk.activity.MainActivity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap


/**
 * Created by heo on 2018. 3. 31..
 */

@Module(subcomponents = [MainComponent::class])
internal abstract class AppModule {

//    @Singleton
//    @Binds
//    internal abstract fun bindDataSource(dataSource: DataSourceImpl): DataSource

    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    internal abstract fun bindMainActivity(builder: MainComponent.Builder): AndroidInjector.Factory<out Activity>
}