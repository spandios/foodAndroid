package com.example.fooddeuk.util

import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults

/**
 * Created by heojuyeong on 2017. 9. 19..
 */

object RealmUtil {

    fun beginTranscation(callback : () -> Boolean){
        val realm = Realm.getDefaultInstance()
        try {
            realm.beginTransaction()
            if(callback()){
                realm.commitTransaction()
            }

        } catch (e: Throwable) {
            if (realm.isInTransaction) {
                realm.cancelTransaction()
            }
            e.printStackTrace()
        }
    }
    

    fun <T : RealmObject> getRealmObject(data: T): T? {
        val realm = Realm.getDefaultInstance()
        try {

            realm.beginTransaction()
            val realmResult = realm.copyToRealm(data)
            realm.commitTransaction()
            return realmResult
        } catch (e: Throwable) {
            if (realm.isInTransaction) {
                realm.cancelTransaction()
            }
            e.printStackTrace()
        }

        return null

    }

    fun <T : RealmObject> getDataSize(clazz: Class<T>): Int {
        val realm = Realm.getDefaultInstance()
        try {

            realm.beginTransaction()
            val size = realm.where(clazz).findAll().size
            realm.commitTransaction()
            return size

        } catch (e: Throwable) {
            if (realm.isInTransaction) {
                realm.cancelTransaction()
            }
            e.printStackTrace()
        }

        return 0
    }

    fun <T : RealmObject> insertData(data: T) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { realm -> realm.copyToRealmOrUpdate(data) }
    }


    fun <T : RealmObject> findDataAll(clazz: Class<T>): RealmResults<T>? {
        val realm = Realm.getDefaultInstance()
        try {
            realm.beginTransaction()
            val item = realm.where(clazz).findAll()
            realm.commitTransaction()
            return item

        } catch (e: Throwable) {
            if (realm.isInTransaction) {
                realm.cancelTransaction()
            }
            e.printStackTrace()
        }

        return null
    }

    fun <T : RealmObject> findData(clazz: Class<T>): T? {
        val realm = Realm.getDefaultInstance()
        try {
            realm.beginTransaction()
            val item = realm.where(clazz).findFirst()
            realm.commitTransaction()
            return item

        } catch (e: Throwable) {
            if (realm.isInTransaction) {
                realm.cancelTransaction()
            }
            e.printStackTrace()
        }

        return null
    }

    fun <T : RealmObject> findDataById(clazz: Class<T>, id: String): RealmResults<T> {
        val realm = Realm.getDefaultInstance()
        return realm.where(clazz).equalTo("id", id).findAll()
    }

    fun <T : RealmObject> removeDataAll(clazz: Class<T>) {
        val realm = Realm.getDefaultInstance()
        val result = findDataAll(clazz)
        realm.executeTransaction { result!!.deleteAllFromRealm() }
    }

    fun <T : RealmObject> removeDataById(clazz: Class<T>, id: String) {
        val realm = Realm.getDefaultInstance()
        val result = findDataById(clazz, id)
        realm.executeTransaction { result.deleteAllFromRealm() }
    }


    //Auto Increment id
    fun <T : RealmObject> getAutoIncrementId(clazz: Class<T>): Int {
        val realm = Realm.getDefaultInstance()
        try {
            val id = realm.where(clazz).max("id")
            return if (id != null) {
                id.toInt() + 1
            } else {
                0
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            return 0
        }

    }


}
