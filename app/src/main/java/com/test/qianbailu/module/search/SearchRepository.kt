package com.test.qianbailu.module.search

import com.test.qianbailu.model.ApiService
import com.test.qianbailu.model.bean.Counts
import com.test.qianbailu.model.bean.VideoCover
import com.test.qianbailu.utils.html2VideoCoverCounts
import io.reactivex.Observable

class SearchDataSourceRepository(
    private val remote: SearchRemoteDataSource
) {
    fun searchVideo(keyword: String, page: Int): Observable<Counts<VideoCover>> {
        return remote.searchVideo(keyword, page)
    }
}

class SearchRemoteDataSource(private val api: ApiService) {
    fun searchVideo(keyword: String, page: Int): Observable<Counts<VideoCover>> {
        return api.searchVideo(keyword, page)
            .map { it.string() }
            .map {
                it.html2VideoCoverCounts()
            }
    }
}