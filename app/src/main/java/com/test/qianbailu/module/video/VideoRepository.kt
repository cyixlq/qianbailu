package com.test.qianbailu.module.video

import com.test.qianbailu.model.ApiService
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
        return api.getVideoHtml(videoId)
            .map { it.string() }
            .map {
                it.html2Video()
            }
    }

}