package com.test.qianbailu.module.home

import com.test.qianbailu.model.bean.Counter
import com.test.qianbailu.model.bean.IHtmlConverter
import com.test.qianbailu.model.bean.VideoCover
import io.reactivex.Observable
import top.cyixlq.core.utils.RxSchedulers

class HomeDataSourceRepository(
    private val remote: HomeRemoteDataSource
) {
    fun getIndexVideoCovers(): Observable<Counter<VideoCover>> {
        return remote.getIndexVideoCovers()
    }
}

class HomeRemoteDataSource(private val converter: IHtmlConverter) {

    fun getIndexVideoCovers(): Observable<Counter<VideoCover>> {
        return Observable.create<Counter<VideoCover>> {
            it.onNext(converter.getHomeVideoCovers())
            it.onComplete()
        }.subscribeOn(RxSchedulers.io)
    }

}