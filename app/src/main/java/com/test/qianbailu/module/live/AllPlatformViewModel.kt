package com.test.qianbailu.module.live

import androidx.lifecycle.MutableLiveData
import com.uber.autodispose.autoDisposable
import top.cyixlq.core.common.viewmodel.CommonViewModel
import top.cyixlq.core.utils.RxSchedulers

class AllPlatformViewModel(
    private val repo: AllPlatformDataSourceRepository,
    val viewState: MutableLiveData<AllPlatformViewState>
) : CommonViewModel() {

    fun getAllLivePlatforms() {
        repo.getAllLivePlatforms()
            .doOnSubscribe { viewState.postValue(AllPlatformViewState(isLoading = true)) }
            .subscribeOn(RxSchedulers.io)
            .observeOn(RxSchedulers.ui)
            .autoDisposable(this)
            .subscribe({
                viewState.value = AllPlatformViewState(platforms = it)
            }, {
                viewState.value = AllPlatformViewState(throwable = it)
            })
    }

}