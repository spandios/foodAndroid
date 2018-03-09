package com.example.fooddeuk.network

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiPredicate
import io.reactivex.schedulers.Schedulers
import java.net.SocketTimeoutException

/**
 * Created by heo on 2018. 2. 10..
 */
object HTTP {


    fun basic(): BiPredicate<Int, Throwable> {
        return BiPredicate { retryCount, throwable -> retryCount <= 1 && throwable is SocketTimeoutException }
    }

    fun socket(): BiPredicate<Int, Throwable> {
        return BiPredicate { retryCount, throwable -> retryCount <= 2 && throwable is SocketTimeoutException }
    }

    fun <T> Single(single: Single<T>): Single<T> {
        return single.retry(basic()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    //not emits value  ->  onComplete or onError
    fun Completable(completable: Completable): Completable {
        return completable.retry(basic()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
    }

    fun <T> Flowable(flowable: Flowable<T>): Flowable<T> {
        return flowable.retry(basic()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun <T> singleTransformer(): SingleTransformer<T, T> {
        return SingleTransformer { upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
    }
    fun <T> completableTansFormer(): CompletableTransformer {
        return CompletableTransformer { upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
    }
    fun <T> flowableTransformer(): FlowableTransformer<T, T> {
        return FlowableTransformer { upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
    }

    







}