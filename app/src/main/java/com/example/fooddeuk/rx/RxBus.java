package com.example.fooddeuk.rx;


import android.support.annotation.NonNull;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by heojuyeong on 2017. 9. 21..
 */

public class RxBus {
    private static final PublishSubject<Object> sSubject = PublishSubject.create();

    private RxBus() {
        // hidden constructor
    }


    public static Disposable subscribe(@NonNull Consumer<Object> action) {
        return sSubject.subscribe(action);
    }

    public static void publish(@NonNull Object message) {
        sSubject.onNext(message);
    }





}
