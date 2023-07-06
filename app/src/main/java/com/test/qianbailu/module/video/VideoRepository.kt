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
    fun getVideo(videoId: String): Observable<Video> {
        return remote.getVideo(videoId)
    }

    fun getVideoHistory(videoId: String) =
        local.getVideoHistory(videoId)

    fun saveVideoHistory(videoCover: VideoCover) =
        local.saveVideoHistory(videoCover)
}

class VideoRemoteDataSource(private val converter: IHtmlConverter) {

    fun getVideo(videoId: String): Observable<Video> {
        return Observable.create {
            try {
                it.onNext(converter.getVideo(videoId))
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }
        }
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