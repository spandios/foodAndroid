package com.iwedding.app.helper

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.fooddeuk.common.CommonApplication
import com.example.fooddeuk.common.CommonApplication.pref

import java.util.HashSet

/**
 * Created by ifamily on 2016-10-19.
 */
class LoginPrefHelper() {
    
    /**
     * 키에 해당하는 데이터(문자열)를 삽입한다.
     * @param key        키
     * @param value    문자열
     */


    companion object {
        fun put(key: String, value: String) {
            val editor = pref.edit()
            editor.putString(key, value)
            editor.commit()
        }

        fun put(key: String, value: Int) {
            val editor = pref.edit()
            editor.putInt(key, value)
            editor.commit()
        }

        fun put(key: String, value: Boolean?) {
            val editor = pref.edit()
            editor.putBoolean(key, value!!)
            editor.commit()
        }



        /**
         * 키에 해당하는 데이터를 가저온다.
         * @param key                키
         * @param defaultValue        기본 값
         * @return
         */
        fun getValue(key: String, defaultValue: String): String? {

            try {
                return pref.getString(key, defaultValue)
            } catch (e: Exception) {
                return defaultValue
            }

        }

        fun getValue(key: String, defaultValue: Boolean?): Boolean? {

            try {
                return pref.getBoolean(key, defaultValue!!)
            } catch (e: Exception) {
                return defaultValue
            }

        }

        fun getValue(key: String, defaultValue: Int): Int {

            try {
                return pref.getInt(key, defaultValue)
            } catch (e: Exception) {
                return defaultValue
            }

        }


        /**
         * 키에 해당하는 데이터를 삭제한다.
         * @param key 키
         */
        fun removePreferences(key: String) {
            val editor = pref.edit()
            editor.remove(key)
            editor.commit()
        }

        fun deleteData() {
            val editor = pref.edit()
            editor.clear()
            editor.commit()
        }

        ///////////////////////////////////////////////////////////////////////////////////////////

    }

}
