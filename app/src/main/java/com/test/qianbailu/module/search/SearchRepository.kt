package com.test.qianbailu.module.search

import com.test.qianbailu.model.*
import com.test.qianbailu.model.bean.Counts
import com.test.qianbailu.model.bean.VideoCover
import com.test.qianbailu.utils.html2VideoCoverCounts
import io.reactivex.Observable
import top.cyixlq.core.utils.CLog

class SearchDataSourceRepository(
    private val remote: SearchRemoteDataSource
) {
    fun searchVideo(keyword: String, page: Int): Observable<Counts<VideoCover>> {
        return remote.searchVideo(keyword, page)
    }
}

class SearchRemoteDataSource(private val api: ApiService) {
    fun searchVideo(keyword: String, page: Int): Observable<Counts<VideoCover>> {
        return Observable.just(HOST)
            .flatMap(FlatmapOrError(api.searchVideo(HOST + BASE_SEARCH_URL, keyword, page)))
            .retryWhen(NoHostRetry(api, api.searchVideo(HOST + BASE_SEARCH_URL, keyword, page)))
            .map {
                val html = it.string()
                html.html2VideoCoverCounts()
            }
    }
}