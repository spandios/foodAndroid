package com.example.heojuyeong.foodandroid.util;

import com.example.heojuyeong.foodandroid.model.restaurant.RestaurantItemRealm;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by heojuyeong on 2017. 9. 19..
 */

public class RealmUtil {
    static Realm realm=Realm.getDefaultInstance();

    public static <T extends RealmObject> T getRealmObject(final T data){
        return realm.copyToRealm(data);
    }
    public static <T extends RealmObject> int getDataSize(Class<T> clazz){
        int size= realm.where(clazz).findAll().size();
        return size;
    }
    public static <T extends RealmObject>void  insertData(final T data){

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(data);
            }
        });
    }


    public static <T extends RealmObject> RealmResults<T> findDataAll(Class<T> clazz) {
        RealmResults<T> item = realm.where(clazz).findAll();
        return item;
    }

    public static <T extends RealmObject> RealmResults<T> findDataById(Class<T> clazz, int id){
        RealmResults<T> item=realm.where(clazz).equalTo("id",id).findAll();
        return item;
    }

    public static <T extends RealmObject>void removeDataAll(Class<T> clazz){
        final RealmResults<T> result = findDataAll(clazz);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                result.deleteAllFromRealm();
            }
        });
    }

    public static <T extends RealmObject>void removeDataById(Class<T> clazz, int id){
        final RealmResults<T> result = findDataById(clazz,id);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                result.deleteAllFromRealm();
            }
        });
    }

    public static RealmResults<RestaurantItemRealm> getRestaurantRealm(){
        return findDataAll(RestaurantItemRealm.class);
    }

    //Auto Increment id
    public static<T extends RealmObject> int getAutoIncrementId(Class<T> clazz) {
        try {
            Number id = realm.where(clazz).max("id");
            if (id != null) {
                return id.intValue() + 1;
            } else {
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }




}
