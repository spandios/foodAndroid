package com.example.fooddeuk.util

import com.example.fooddeuk.`object`.GlobalApplication
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults

/**
 * Created by heojuyeong on 2017. 9. 19..
 */

object RealmUtil {
    val realm: Realm = GlobalApplication.getInstance().realm
    val inRealm: Realm = GlobalApplication.getInstance().inRealm

    fun beginTranscation(callback : () -> Boolean){

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

        realm.executeTransaction { realm -> realm.copyToRealmOrUpdate(data) }
    }


    fun <T : RealmObject> insertInData(data: T) {

        inRealm.executeTransaction { realm -> realm.copyToRealmOrUpdate(data) }
    }


    fun <T : RealmObject> findDataAll(clazz: Class<T>): RealmResults<T>? {

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

        return realm.where(clazz).equalTo("id", id).findAll()
    }

    fun <T : RealmObject> removeDataAll(clazz: Class<T>) {

        val result = findDataAll(clazz)
        realm.executeTransaction { result!!.deleteAllFromRealm() }
    }

    fun <T : RealmObject> removeDataById(clazz: Class<T>, id: String) {

        val result = findDataById(clazz, id)
        realm.executeTransaction { result.deleteAllFromRealm() }
    }


    //Auto Increment id
    fun <T : RealmObject> getAutoIncrementId(clazz: Class<T>): Int {

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
