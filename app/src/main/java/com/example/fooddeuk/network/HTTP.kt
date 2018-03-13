package com.example.fooddeuk.network

import com.example.fooddeuk.GlobalApplication
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiPredicate
import io.reactivex.schedulers.Schedulers
import java.net.SocketTimeoutException

/**
 * Created by heo on 2018. 2. 10..
 */
object HTTP {

    var httpService: HttpService =  GlobalApplication.getInstance().retrofit.create(HttpService::class.java)

    fun basic(): BiPredicate<Int, Throwable> {
        return BiPredicate { retryCount, throwable -> retryCount <= 1 && throwable is SocketTimeoutException }
    }

    fun socket(): BiPredicate<Int, Throwable> {
        return BiPredicate { retryCount, throwable -> retryCount <= 2 && throwable is SocketTimeoutException }
    }

    fun <T> single(single: Single<T>): Single<T> {
        return single.retry(basic()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    //not emits value  ->  onComplete or onError
    fun completable(completable: Completable): Completable {
        return completable.retry(basic()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
    }

    fun <T> flowable(flowable: Flowable<T>): Flowable<T> {
        return flowable.retry(basic()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    fun <T> singleAsync(): SingleTransformer<T, T> {
        return SingleTransformer { single -> single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
    }

    fun <T> observable(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun complete(): CompletableTransformer {
        return CompletableTransformer { completable -> completable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
    }

    







}