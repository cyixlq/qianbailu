package com.test.qianbailu.module.home

import com.test.qianbailu.model.Repo
import com.test.qianbailu.model.bean.VideoCover
import com.test.qianbailu.utils.html2VideoCoverList
import io.reactivex.Observable

class HomeDataSourceRepository(
    private val remote: HomeRemoteDataSource = HomeRemoteDataSource()
) {
    fun getIndexVideoCovers(): Observable<MutableList<VideoCover>> {
        return remote.getIndexVideoCovers()
    }
}

class HomeRemoteDataSource {

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