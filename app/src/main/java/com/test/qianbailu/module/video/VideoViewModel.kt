package com.test.qianbailu.module.video

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uber.autodispose.autoDisposable
import top.cyixlq.core.common.viewmodel.CommonViewModel
import top.cyixlq.core.utils.RxSchedulers

class VideoViewModel(private val repo: VideoDataSourceRepository) : CommonViewModel() {

    val viewState = MutableLiveData<VideoViewState>()

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

@Suppress("UNCHECKED_CAST")
class VideoViewModelFactory(private val repo: VideoDataSourceRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VideoViewModel(repo) as T
    }
}