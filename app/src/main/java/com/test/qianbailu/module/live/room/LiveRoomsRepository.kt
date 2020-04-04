package com.test.qianbailu.module.live.room

import com.test.qianbailu.model.ApiService
import com.test.qianbailu.model.bean.LiveRooms
import io.reactivex.Observable

class LiveRoomsDataSourceRepository(
    private val remote: LiveRoomsRemoteDataSource
) {
    fun getLiveRooms(platformPath: String): Observable<LiveRooms> {
        return remote.getLiveRooms(platformPath)
    }
}

class LiveRoomsRemoteDataSource(private val api: ApiService) {
    fun getLiveRooms(platformPath: String): Observable<LiveRooms> {
        return api.getLiveRooms(platformPath)
    }
}