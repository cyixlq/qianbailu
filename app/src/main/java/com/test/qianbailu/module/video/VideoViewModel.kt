package com.test.qianbailu.module.video

import androidx.lifecycle.MutableLiveData
import com.uber.autodispose.autoDisposable
import top.cyixlq.core.common.viewmodel.CommonViewModel
import top.cyixlq.core.utils.RxSchedulers

class VideoViewModel(
    private val repo: VideoDataSourceRepository,
    val viewState: MutableLiveData<VideoViewState>
) : CommonViewModel() {

    fun getVideo(videoId: String) {
        repo.getVideo(videoId)
            .doOnSubscribe { viewState.postValue(VideoViewState(isLoading = true)) }
            .subscribeOn(RxSchedulers.io)
            .observeOn(RxSchedulers.ui)
            .autoDisposable(this)
            .subscribe({
                viewState.postValue(VideoViewState(isLoading = false, video = it))
            }, {
                viewState.postValue(VideoViewState(isLoading = false, throwable = it))
            })
    }

}