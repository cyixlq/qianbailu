package com.test.qianbailu.module.home

import com.test.qianbailu.model.ApiService
import com.test.qianbailu.model.HOST
import com.test.qianbailu.model.bean.VideoCover
import com.test.qianbailu.utils.html2Host
import com.test.qianbailu.utils.html2VideoCoverList
import io.reactivex.Observable

class HomeDataSourceRepository(
    private val remote: HomeRemoteDataSource
) {
    fun getIndexVideoCovers(): Observable<MutableList<VideoCover>> {
        return remote.getIndexVideoCovers()
    }
}

class HomeRemoteDataSource(private val api: ApiService) {

    fun getIndexVideoCovers(): Observable<MutableList<VideoCover>> {
        return api.getHost()
            .flatMap {
                val html = it.string()
                val host = html.html2Host()
                HOST = host
                api.getIndexHtml(host)
            }.map{
                it.string()
            }
            .map {
                return@map it.html2VideoCoverList()
            }
    }

}