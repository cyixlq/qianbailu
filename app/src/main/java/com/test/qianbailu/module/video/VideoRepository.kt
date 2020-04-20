package com.test.qianbailu.module.video

import com.test.qianbailu.model.ApiService
import com.test.qianbailu.model.FlatmapOrError
import com.test.qianbailu.model.HOST
import com.test.qianbailu.model.NoHostRetry
import com.test.qianbailu.model.bean.Video
import com.test.qianbailu.utils.html2Video
import io.reactivex.Observable

class VideoDataSourceRepository(
    private val remote: VideoRemoteDataSource
) {
    fun getVideo(videoId: String): Observable<Video> {
        return remote.getVideo(videoId)
    }
}

class VideoRemoteDataSource(private val api: ApiService) {

    fun getVideo(videoId: String): Observable<Video> {
        return Observable.just(HOST)
            .flatMap(FlatmapOrError(api.getVideoHtml(HOST + videoId)))
            .retryWhen(NoHostRetry(api, api.getVideoHtml(HOST + videoId)))
            .map {
                val html = it.string()
                html.html2Video()
            }
    }

}