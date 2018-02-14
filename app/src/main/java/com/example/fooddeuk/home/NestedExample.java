package com.example.fooddeuk.home;

import android.support.annotation.CheckResult;
import android.support.v4.widget.NestedScrollView;

import com.jakewharton.rxbinding2.view.ViewScrollChangeEvent;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

/**
 * Created by heo on 2018. 2. 14..
 */

public class NestedExample {


        /**
         * Create an observable of scroll-change events for {@code view}.
         * <p>
         * <em>Warning:</em> The created observable keeps a strong reference to {@code view}.
         * Unsubscribe to free this reference.
         */
        @CheckResult
        @NonNull public static Observable<ViewScrollChangeEvent> scrollChangeEvents(
                @NonNull NestedScrollView view) {
            return new NestedScrollViewScrollChangeEventObservable(view);
        }



}
