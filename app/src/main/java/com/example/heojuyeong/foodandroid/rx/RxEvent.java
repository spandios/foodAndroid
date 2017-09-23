package com.example.heojuyeong.foodandroid.rx;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by heojuyeong on 2017. 9. 21..
 */

public class RxEvent {
    private static RxEvent mInstance;
    private PublishSubject<Object> mSubject;

    private RxEvent() {
        mSubject = PublishSubject.create();
    }

    public static RxEvent getInstance() {
        if (mInstance == null) {
            mInstance = new RxEvent();
        }
        return mInstance;
    }

    public void sendEvent(Object object) {
        mSubject.onNext(object);
    }

    public Observable<Object> getObservable() {
        return mSubject;
    }


}
