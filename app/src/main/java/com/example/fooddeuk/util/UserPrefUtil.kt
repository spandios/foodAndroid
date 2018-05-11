package com.iwedding.app.helper

import android.content.Context
import android.content.SharedPreferences


class UserPrefUtil {

    /**
     * 키에 해당하는 데이터(문자열)를 삽입한다.
     * @param key        키
     * @param value    문자열
     */

    companion object {
        const val PROVIDER_ID = "provider_id"
        const val EMAIL = "email"
        const val NAME = "name"
        const val FCM_TOKEN = "fcm_token"
        const val PHONE = "phone"
        const val PROVIDER = "provider"
        const val EXIST = "EXIST"

        lateinit var userPref: SharedPreferences

        fun setUserPref(context: Context, key : String){
            userPref =context.getSharedPreferences(key,Context.MODE_PRIVATE)
        }

        fun setValue(key: String, value: String) {
            val editor = userPref.edit()
            editor.putString(key, value)
            editor.commit()
        }

        fun setValue(key: String, value: Int) {
            val editor = userPref.edit()
            editor.putInt(key, value)
            editor.commit()
        }

        fun setValue(key: String, value: Boolean?) {
            val editor = userPref.edit()
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
                return userPref.getString(key, defaultValue)
            } catch (e: Exception) {
                return defaultValue
            }

        }

        fun getValue(key: String, defaultValue: Boolean): Boolean {

            try {
                return userPref.getBoolean(key, defaultValue)
            } catch (e: Exception) {
                return defaultValue
            }

        }

        fun getValue(key: String, defaultValue: Int): Int {

            try {
                return userPref.getInt(key, defaultValue)
            } catch (e: Exception) {
                return defaultValue
            }

        }


        /**
         * 키에 해당하는 데이터를 삭제한다.
         * @param key 키
         */
        fun removePreferences(key: String) {
            val editor = userPref.edit()
            editor.remove(key)
            editor.commit()
        }

        fun deleteData() {
            val editor = userPref.edit()
            editor.clear()
            editor.commit()
        }

        ///////////////////////////////////////////////////////////////////////////////////////////

    }

}
