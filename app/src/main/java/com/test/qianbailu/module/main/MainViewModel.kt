package com.test.qianbailu.module.main

import androidx.lifecycle.MutableLiveData
import com.uber.autodispose.autoDisposable
import top.cyixlq.core.common.viewmodel.CommonViewModel
import top.cyixlq.core.utils.CLog
import top.cyixlq.core.utils.toastShort

class MainViewModel(
    private val repo: MainDataSourceRepository,
    val viewState: MutableLiveData<MainViewState>
) : CommonViewModel() {

    fun getVersionInfo() {
        repo.getVersionInfo()
            .autoDisposable(this)
            .subscribe({
                viewState.postValue(MainViewState(updateAppBean = it))
            }, {
                CLog.e("版本更新检查出错：${it.localizedMessage}")
                "版本更新检查出错".toastShort()
            })
    }

}