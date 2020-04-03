package com.test.qianbailu.module.live.room

import androidx.lifecycle.MutableLiveData
import com.uber.autodispose.autoDisposable
import top.cyixlq.core.common.viewmodel.CommonViewModel
import top.cyixlq.core.utils.RxSchedulers

class LiveRoomsViewModel(
    private val repo: LiveRoomsDataSourceRepository,
    val viewState: MutableLiveData<LiveRoomsViewState>
) : CommonViewModel() {

    fun getLiveRooms(platformPath: String) {
        repo.getLiveRooms(platformPath)
            .doOnSubscribe { viewState.postValue(LiveRoomsViewState(isLoading = true)) }
            .subscribeOn(RxSchedulers.io)
            .observeOn(RxSchedulers.ui)
            .autoDisposable(this)
            .subscribe({
                viewState.value = LiveRoomsViewState(liveRooms = it)
            }, {
                viewState.value = LiveRoomsViewState(throwable = it)
            })
    }

}
