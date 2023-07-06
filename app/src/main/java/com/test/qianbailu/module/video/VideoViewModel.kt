package com.test.qianbailu.module.video

import androidx.lifecycle.MutableLiveData
import com.test.qianbailu.model.bean.VideoCover
import com.uber.autodispose.autoDisposable
import top.cyixlq.core.common.viewmodel.CommonViewModel
import top.cyixlq.core.utils.RxSchedulers

class VideoViewModel(
    private val repo: VideoDataSourceRepository,
    val viewState: MutableLiveData<VideoViewState>
) : CommonViewModel() {

    val videoHistory: MutableLiveData<VideoCover?> = MutableLiveData()

    fun getVideo(videoId: String?) {
        videoId ?: return
        repo.getVideo(videoId)
            .doOnSubscribe { viewState.postValue(VideoViewState(isLoading = true)) }
            .subscribeOn(RxSchedulers.io)
            .observeOn(RxSchedulers.ui)
            .doOnComplete { viewState.value = VideoViewState(isLoading = false) }
            .autoDisposable(this)
            .subscribe({
                viewState.value = VideoViewState(video = it)
            }, {
                viewState.value = VideoViewState(throwable = it)
            })
    }

    fun getVideoHistory(videoId: String) {
        repo.getVideoHistory(videoId)
            .subscribeOn(RxSchedulers.io)
            .observeOn(RxSchedulers.ui)
            .autoDisposable(this)
            .subscribe({
                videoHistory.value = it
            }, {
                videoHistory.value = null
            })
    }

    fun saveProgress(videoCover: VideoCover?) {
        videoCover ?: return
        repo.saveVideoHistory(videoCover)
            .subscribeOn(RxSchedulers.io)
            .observeOn(RxSchedulers.io)
            .autoDisposable(this)
            .subscribe()
    }

}