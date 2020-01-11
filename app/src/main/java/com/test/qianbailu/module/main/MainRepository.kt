package com.test.qianbailu.module.main

import com.test.qianbailu.model.Repo
import com.test.qianbailu.model.bean.VideoCover
import com.test.qianbailu.utils.html2VideoCoverList
import io.reactivex.Observable

class MainDataSourceRepository(
    private val remote: MainRemoteDataSource = MainRemoteDataSource()
) {
    fun getIndexVideoCovers(): Observable<MutableList<VideoCover>> {
        return remote.getIndexVideoCovers()
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

}