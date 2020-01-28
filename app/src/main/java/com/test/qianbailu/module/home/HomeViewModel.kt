package com.test.qianbailu.module.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uber.autodispose.autoDisposable
import top.cyixlq.core.common.viewmodel.CommonViewModel
import top.cyixlq.core.utils.RxSchedulers

class HomeViewModel(private val repo: HomeDataSourceRepository) : CommonViewModel() {

    val viewState = MutableLiveData<HomeViewState>()

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
                    viewState.postValue(HomeViewState(isLoading = false, list = it))
                },
                {
                    viewState.postValue(HomeViewState(isLoading = false, throwable = it))
                }
            )
    }
}

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(private val repo: HomeDataSourceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repo) as T
    }
}