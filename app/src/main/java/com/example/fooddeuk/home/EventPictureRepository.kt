package com.example.fooddeuk.home

import com.example.fooddeuk.GlobalApplication.httpService
import com.example.fooddeuk.network.HTTP
import io.reactivex.Single

/**
 * Created by heo on 2018. 2. 27..
 */

interface EventPictureDataSource {
    fun getEventPicture(): Single<HomeEventPictureResponse>

}

object EventPictureRemote : EventPictureDataSource {
    override fun getEventPicture(): Single<HomeEventPictureResponse> =
            HTTP.Single(httpService.homeEvent)

}


object EventPictureRepository : EventPictureDataSource {
    private var isDirty = true
    private var memory: Single<HomeEventPictureResponse>? = null

    override fun getEventPicture(): Single<HomeEventPictureResponse> {
        //Cached!
        if (!isDirty && memory != null) {
            return memory as Single<HomeEventPictureResponse>
        }

        val remoteData = getRemotePictureDataAndCache()
        return remoteData
    }


    private fun getRemotePictureDataAndCache(): Single<HomeEventPictureResponse> {
        return EventPictureRemote.getEventPicture().apply {
            memory=this
            isDirty=false
        }

    }

    fun refresh() {
        isDirty = true
    }

}