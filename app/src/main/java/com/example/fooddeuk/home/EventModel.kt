package com.example.fooddeuk.home

import io.realm.annotations.PrimaryKey

/**
 * Created by heo on 2018. 2. 27..
 */
data class HomeEventPictureResponse(@PrimaryKey var version : String ="", var eventPictureList: ArrayList<String> = ArrayList())

