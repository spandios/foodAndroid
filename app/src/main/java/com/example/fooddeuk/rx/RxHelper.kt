package com.example.fooddeuk.rx

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiPredicate
import io.reactivex.schedulers.Schedulers
import java.net.SocketTimeoutException

/**
 * Created by heo on 2018. 2. 10..
 */

object RxHelper {
    @JvmStatic
    private fun basic(): BiPredicate<Int, Throwable> {
        return BiPredicate { retryCount, throwable -> retryCount <= 1 && throwable is SocketTimeoutException }
    }
    @JvmStatic
    private fun socket(): BiPredicate<Int, Throwable> {
        return BiPredicate { retryCount, throwable -> retryCount <= 2 && throwable is SocketTimeoutException }
    }
    @JvmStatic
    fun <T> Single(single: Single<T>): Single<T> {
        return single.retry(basic()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
    @JvmStatic
    //not emits value  ->  onComplete or onError
    fun Completable(completable: Completable): Completable {
        return completable.retry(basic()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
    @JvmStatic
    fun <T> Flowable(flowable: Flowable<T>): Flowable<T> {
        return flowable.retry(basic()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}
