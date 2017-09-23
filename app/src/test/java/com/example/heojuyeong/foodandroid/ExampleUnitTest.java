package com.example.heojuyeong.foodandroid;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Before
    public void before(){

    }

    @After
    public void after(){

    }

    @Test
    public void test(){


    }
//        CompositeSubscription subscriptions = new CompositeSubscription();
//
//
//        Observable<String> testRx=Observable.create(subscriber -> {
//            subscriber.onNext("testObserble");
//            subscriber.onCompleted();
//        });
//
//        //subscriber cant' reuse   observer can reuse
//        Subscriber<String> subscriber=new Subscriber<String>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(String s) {
//
//            }
//        };
//
////        Subscriber<T> subscription=testRx.subscribe(new Subscriber<String>() {
////            @Override
////            public void onCompleted() {
////
////            }
////
////            @Override
////            public void onError(Throwable e) {
////
////            }
////
////            @Override
////            public void onNext(String s) {
////
////            }
////        });
//        testRx.subscribe(new Subscriber<String>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(String s) {
//
//            }
//        });
//
//        PublishSubject<String> publishSubject=PublishSubject.create();
//        publishSubject.subscribe(new Observer<String>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(String s) {
//                System.out.print(s);
//            }
//        });
//
//        publishSubject.onNext("1");
//        publishSubject.onNext("1");
//        publishSubject.onNext("1");
//
//    }



}