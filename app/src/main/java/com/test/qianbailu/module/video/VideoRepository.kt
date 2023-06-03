package com.test.qianbailu.module.video

import com.test.qianbailu.model.bean.IHtmlConverter
import com.test.qianbailu.model.bean.Video
import io.reactivex.Observable
import top.cyixlq.core.utils.RxSchedulers

class VideoDataSourceRepository(
    private val remote: VideoRemoteDataSource
) {
    fun getVideo(videoId: String): Observable<Video> {
        return remote.getVideo(videoId)
    }
}

class VideoRemoteDataSource(private val converter: IHtmlConverter) {

    fun getVideo(videoId: String): Observable<Video> {
        return Observable.create {
            it.onNext(converter.getVideo(videoId))
            it.onComplete()
        }.subscribeOn(RxSchedulers.io)
    }

}