package com.zm.video.module.video

import com.zm.video.model.Repo
import com.zm.video.model.bean.Video
import com.zm.video.utils.html2Video
import io.reactivex.Observable


class VideoDataSourceRepository(
    private val remote: VideoRemoteDataSource = VideoRemoteDataSource()
) {
    fun getVideo(videoId: String): Observable<Video> {
        return remote.getVideo(videoId)
    }
}

class VideoRemoteDataSource {

    fun getVideo(videoId: String): Observable<Video> {
        return Repo.api.getVideoHtml(videoId)
            .map { it.string() }
            .map {
                it.html2Video()
            }
    }

}