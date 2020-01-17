package com.zm.video.module.main

import com.zm.video.model.Repo
import com.zm.video.model.bean.UpdateAppBean
import com.zm.video.model.bean.VideoCover
import com.zm.video.utils.html2VideoCoverList
import io.reactivex.Observable

class MainDataSourceRepository(
    private val remote: MainRemoteDataSource = MainRemoteDataSource()
) {
    fun getIndexVideoCovers(): Observable<MutableList<VideoCover>> {
        return remote.getIndexVideoCovers()
    }

    fun getVersionInfo(): Observable<UpdateAppBean> {
        return remote.getVersionInfo()
    }
}

class MainRemoteDataSource {

    fun getIndexVideoCovers(): Observable<MutableList<VideoCover>> {
        return Repo.api.getIndexHtml()
            .map{
                it.string()
            }
            .map {
                return@map it.html2VideoCoverList()
            }
    }

    fun getVersionInfo(): Observable<UpdateAppBean> {
        return Repo.api
            .getVersionInfo()
    }

}