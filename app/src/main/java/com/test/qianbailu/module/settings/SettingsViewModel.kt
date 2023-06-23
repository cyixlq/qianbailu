package com.test.qianbailu.module.settings

import androidx.lifecycle.MutableLiveData
import com.uber.autodispose.autoDisposable
import top.cyixlq.core.common.viewmodel.CommonViewModel
import top.cyixlq.core.utils.RxSchedulers

class SettingsViewModel(
    private val repo: SettingsRepository,
    val mViewState: MutableLiveData<SettingsViewState>
) : CommonViewModel() {

    fun getAllHistory() {
        repo.getAllVideoHistory()
            .subscribeOn(RxSchedulers.io)
            .subscribeOn(RxSchedulers.ui)
            .autoDisposable(this)
            .subscribe({
                mViewState.postValue(SettingsViewState(videoHistory = it))
            }, {})
    }

}