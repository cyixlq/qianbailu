package com.test.qianbailu.module.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uber.autodispose.autoDisposable
import top.cyixlq.core.common.viewmodel.CommonViewModel
import top.cyixlq.core.utils.RxSchedulers
import top.cyixlq.core.utils.toastShort

class MainViewModel(private val repo: MainDataSourceRepository) : CommonViewModel() {

    val viewState = MutableLiveData<MainViewState>()

    fun getVersionInfo() {
        repo.getVersionInfo()
            .subscribeOn(RxSchedulers.io)
            .observeOn(RxSchedulers.ui)
            .autoDisposable(this)
            .subscribe({
                viewState.postValue(MainViewState(updateAppBean = it))
            }, {
                "版本更新检查出错".toastShort()
            })
    }

}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val repo: MainDataSourceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repo) as T
    }
}