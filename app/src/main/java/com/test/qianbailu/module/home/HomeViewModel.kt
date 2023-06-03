package com.test.qianbailu.module.home

import androidx.lifecycle.MutableLiveData
import com.uber.autodispose.autoDisposable
import top.cyixlq.core.common.viewmodel.CommonViewModel
import top.cyixlq.core.utils.RxSchedulers

class HomeViewModel(
    private val repo: HomeDataSourceRepository,
    val viewState: MutableLiveData<HomeViewState>
) : CommonViewModel() {

    fun getIndexVideoCovers() {
        repo.getIndexVideoCovers()
            .subscribeOn(RxSchedulers.io)
            .observeOn(RxSchedulers.ui)
            .doOnSubscribe{
                viewState.postValue(HomeViewState(isLoading = true))
            }
            .autoDisposable(this)
            .subscribe(
                {
                    viewState.postValue(HomeViewState(isLoading = false, list = it.children))
                },
                {
                    viewState.postValue(HomeViewState(isLoading = false, throwable = it))
                }
            )
    }
}
