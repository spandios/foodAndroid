package com.iwedding.app.helper

import android.content.Context
import android.content.SharedPreferences


class RecentPref {

    companion object {
        lateinit var recentPref : SharedPreferences

        fun setRecentPref(context: Context, key: String){
            recentPref=context.getSharedPreferences(key,Context.MODE_PRIVATE)
        }

        fun setValue(key: String, value: String) {
            val editor = recentPref.edit()
            editor.putString(key, value)
            editor.commit()
        }

        fun setValue(key: String, value: Int) {
            val editor = recentPref.edit()
            editor.putInt(key, value)
            editor.commit()
        }

        fun setValue(key: String, value: Boolean?) {
            val editor = recentPref.edit()
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
                return recentPref.getString(key, defaultValue)
            } catch (e: Exception) {
                return defaultValue
            }

        }

        fun getValue(key: String, defaultValue: Boolean): Boolean {

            try {
                return recentPref.getBoolean(key, defaultValue)
            } catch (e: Exception) {
                return defaultValue
            }

        }

        fun getValue(key: String, defaultValue: Int): Int {

            try {
                return recentPref.getInt(key, defaultValue)
            } catch (e: Exception) {
                return defaultValue
            }

        }


        /**
         * 키에 해당하는 데이터를 삭제한다.
         * @param key 키
         */
        fun removePreferences(key: String) {
            val editor = recentPref.edit()
            editor.remove(key)
            editor.commit()
        }

        fun deleteData() {
            val editor = recentPref.edit()
            editor.clear()
            editor.commit()
        }

        ///////////////////////////////////////////////////////////////////////////////////////////

    }

}
