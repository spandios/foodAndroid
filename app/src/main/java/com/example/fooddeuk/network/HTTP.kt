package com.example.fooddeuk.network

import com.example.fooddeuk.model.user.User
import com.example.fooddeuk.model.user.UserItem
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by iwedding on 2018. 2. 10..
 */
interface HTTP{
    @GET("/api/user/readUser")
    abstract fun getUser(@Query("provider_id") provider_id: String): Call<UserItem>


    @POST("/api/user/createUser")
    abstract fun createUser(@Body user: User): Call<Void>


    @FormUrlEncoded
    @POST("/api/user/updateToken")
    abstract fun updateToken(@Field("provider_id") provider_id: String, @Field("fcm_token") fcm_token: String): Call<Void>







}