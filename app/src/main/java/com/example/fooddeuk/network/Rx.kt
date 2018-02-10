package com.example.fooddeuk.network

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.SocketTimeoutException
import java.util.function.BiPredicate

/**
 * Created by iwedding on 2018. 2. 10..
 */
object Rx {
    @JvmStatic
    fun <T> Single(single : Single<T>) : Single<T> =  single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    @JvmStatic
    fun Completable(completable: Completable) : Completable =  completable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    @JvmStatic
    fun <T> Flowable(single : Flowable<T>) : Flowable<T> =  single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())






}