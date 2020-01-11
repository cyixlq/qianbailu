package com.test.qianbailu.module.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.qianbailu.model.bean.VideoCover
import com.uber.autodispose.autoDisposable
import top.cyixlq.core.common.viewmodel.CommonViewModel
import top.cyixlq.core.utils.RxSchedulers

class MainViewModel(private val repo: MainDataSourceRepository) : CommonViewModel() {

    val viewState = MutableLiveData<MainViewState>()

    fun getIndexVideoCovers() {
        repo.getIndexVideoCovers()
            .subscribeOn(RxSchedulers.io)
            .observeOn(RxSchedulers.ui)
            .doOnSubscribe{
                viewState.postValue(MainViewState(isLoading = true))
            }
            .autoDisposable(this)
            .subscribe(
                {
                    viewState.postValue(MainViewState(isLoading = false, list = it))
                },
                {
                    viewState.postValue(MainViewState(isLoading = false, throwable = it))
                }
            )
    }

}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val repo: MainDataSourceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repo) as T
    }
}