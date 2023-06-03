package com.test.qianbailu.module.search

import com.test.qianbailu.model.bean.Counter
import com.test.qianbailu.model.bean.IHtmlConverter
import com.test.qianbailu.model.bean.VideoCover
import io.reactivex.Observable
import top.cyixlq.core.utils.RxSchedulers

class SearchDataSourceRepository(
    private val remote: SearchRemoteDataSource
) {
    fun searchVideo(keyword: String, page: Int): Observable<Counter<VideoCover>> {
        return remote.searchVideo(keyword, page)
    }
}

class SearchRemoteDataSource(private val converter: IHtmlConverter) {
    fun searchVideo(keyword: String, page: Int): Observable<Counter<VideoCover>> {
        return Observable.create {
            it.onNext(converter.search(keyword, page))
            it.onComplete()
        }.subscribeOn(RxSchedulers.io)
    }
}