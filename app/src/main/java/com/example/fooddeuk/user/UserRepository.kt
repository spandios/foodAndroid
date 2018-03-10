package com.example.fooddeuk.user

import android.annotation.SuppressLint
import com.example.fooddeuk.util.RealmUtil



/**
 * Created by heo on 2018. 3. 3..
 */

interface UserDataSource {
    fun initUser(user : User)
    fun getUser() : User?
}

object UserLocalRepository : UserDataSource{
    @SuppressLint("StaticFieldLeak")
    override fun initUser(user: User) {

    }

    override fun getUser(): User? = RealmUtil.findData(User::class.java)

}

//object UserRemoteRepository : UserDataSource{
//    override fun initUser(user: User) {
//
//    }
//
//    override fun getUser(): User {
//
//    }
//}

object UserRepository : UserDataSource {
    var isDirty = true
    private var user : User? =null
    private var localRepository = UserLocalRepository

    override fun initUser(user: User) {

    }

    override fun getUser(): User?{
        if(!isDirty&&user!=null){
            return user
        }
        isDirty=false
        if(localRepository.getUser()!=null){
            return localRepository.getUser()
        }else{
            return null
        }

    }

    fun clear(){
        isDirty=true
    }



}