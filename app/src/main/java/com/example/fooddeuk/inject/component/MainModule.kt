package com.example.fooddeuk.inject.component

import com.example.fooddeuk.home.HomeContract
import com.example.fooddeuk.home.HomePresenter
import dagger.Binds
import dagger.Module


/**
 * Created by heo on 2018. 3. 31..
 */

@Module
abstract class MainModule {
    @Binds
    abstract fun bindPresenter(mainPresenter: HomePresenter): HomeContract.Presenter
}