package com.test.qianbailu.module.video

import com.test.qianbailu.model.AppDatabase
import com.test.qianbailu.model.bean.IHtmlConverter
import com.test.qianbailu.model.bean.Video
import com.test.qianbailu.model.bean.VideoCover
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class VideoDataSourceRepository(
    private val remote: VideoRemoteDataSource,
    private val local: VideoLocalDataSource
) {
    fun getVideo(videoCover: VideoCover): Observable<Video> {
        return remote.getVideo(videoCover)
    }

    fun getVideoHistory(videoId: String) =
        local.getVideoHistory(videoId)

    fun saveVideoHistory(videoCover: VideoCover) =
        local.saveVideoHistory(videoCover)

    fun getPlayHeader(video: Video) = remote.getPlayHeader(video)
}

class VideoRemoteDataSource(private val converter: IHtmlConverter) {

    fun getVideo(videoCover: VideoCover): Observable<Video> {
        return Observable.create {
            try {
                it.onNext(converter.getVideo(videoCover))
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }

    fun getPlayHeader(video: Video): HashMap<String, String>? {
        return converter.getPlayHeaders(video)
    }
}

class VideoLocalDataSource(private val appDatabase: AppDatabase) {

    fun getVideoHistory(videoId: String): Single<VideoCover?> {
        return appDatabase.videoHistoryDao().findItem(videoId)
    }

    fun saveVideoHistory(videoCover: VideoCover): Completable {
        return appDatabase.videoHistoryDao().insertItem(videoCover)
    }

}