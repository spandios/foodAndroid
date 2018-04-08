package com.example.fooddeuk

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by heo on 2018. 3. 12..
 */

interface BasePresenter {

    var compositeDisposable : CompositeDisposable

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun clearDisposable() {
        compositeDisposable.clear()
    }
}



abstract class BaseRepository {
    var isDirty = true

    fun cacheClear() {
        isDirty = true
    }
}
