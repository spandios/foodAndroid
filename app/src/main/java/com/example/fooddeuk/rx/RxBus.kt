package com.example.fooddeuk.rx


import android.util.SparseArray
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject




/**
 * Created by heojuyeong on 2017. 9. 21..
 */


object RxBus {
    val MapActivityData = 0
    val DetailRestaurantActivityData = 1
    val ReviewActivityData = 2

    private val sSubjectMap = SparseArray<PublishSubject<Any>>()
    private val sSubscriptionsMap = HashMap<Any,CompositeDisposable>()

    fun publish(subject: Int, @NonNull message: Any) {
        getSubject(subject).onNext(message)
    }

    private fun getSubject(subjectCode: Int): PublishSubject<Any> {
        var subject = sSubjectMap.get(subjectCode)
        if (subject == null) {
            subject = PublishSubject.create()
            subject.subscribeOn(AndroidSchedulers.mainThread())
            sSubjectMap.put(subjectCode, subject)
        }
        return subject
    }


    @NonNull
    private fun getCompositeSubscription(@NonNull `object`: Any): CompositeDisposable{
        var compositeSubscription = sSubscriptionsMap[`object`]
        if (compositeSubscription == null) {
            compositeSubscription = CompositeDisposable()
            sSubscriptionsMap[`object`] = compositeSubscription
        }
        return compositeSubscription
    }

    fun unregister(@NonNull lifecycle: Any) {
        //We have to remove the composition from the map, because once you unsubscribe it can't be used anymore
        val compositeDisposable = sSubscriptionsMap.remove(lifecycle)
        compositeDisposable?.dispose()
    }


    fun subscribe(subject: Int, @NonNull lifecycle: Any, @NonNull action: Consumer<Any>) {
        val subscription = getSubject(subject).subscribe(action)
        getCompositeSubscription(lifecycle).add(subscription)
    }


    private val sIntentSubjectMap = SparseArray<BehaviorSubject<Any>>()
    private val sIntentDisposable = HashMap<Class<*>,CompositeDisposable>()

    fun intentPublish(subject: Int,message: Any){
        getIntentSubject(subject).onNext(message)
    }

    private fun getIntentSubject(subjectCode: Int): BehaviorSubject<Any> {
        var subject = sIntentSubjectMap.get(subjectCode)
        if (subject == null) {
            subject = BehaviorSubject.create()
            subject.subscribeOn(AndroidSchedulers.mainThread())
            sIntentSubjectMap.put(subjectCode, subject)
        }
        return subject
    }

    fun intentSubscribe(subject: Int, @NonNull lifecycle: Class<*>, @NonNull action: Consumer<Any>) {
        val subscription = getIntentSubject(subject).subscribe(action)
        getIntentCompositeDisposable(lifecycle).add(subscription)
    }

    private fun getIntentCompositeDisposable(@NonNull clazz: Class<*>): CompositeDisposable{
        var compositeSubscription = sIntentDisposable[clazz]
        if (compositeSubscription == null) {
            compositeSubscription = CompositeDisposable()
            sIntentDisposable[clazz] = compositeSubscription
        }
        return compositeSubscription
    }

    fun intentUnregister(@NonNull lifecycle: Class<*>) {
        //We have to remove the composition from the map, because once you unsubscribe it can't be used anymore
        val compositeDisposable = sIntentDisposable.remove(lifecycle)
        compositeDisposable?.dispose()
    }






}
