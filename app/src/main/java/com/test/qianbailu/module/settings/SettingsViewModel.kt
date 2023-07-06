package com.test.qianbailu.module.settings

import androidx.lifecycle.MutableLiveData
import com.test.qianbailu.model.bean.VideoCover
import com.uber.autodispose.autoDisposable
import top.cyixlq.core.common.viewmodel.CommonViewModel
import top.cyixlq.core.utils.RxSchedulers

class SettingsViewModel(
    private val repo: SettingsRepository,
    val mViewState: MutableLiveData<SettingsViewState>
) : CommonViewModel() {

    fun observeVideoHistoryFirstPage() {
        repo.observeVideoHistoryFirstPage()
            .subscribeOn(RxSchedulers.io)
            .observeOn(RxSchedulers.ui)
            .autoDisposable(this)
            .subscribe({
                mViewState.value = SettingsViewState(videoHistory = it)
            }, {
                mViewState.value = SettingsViewState(videoHistory = mutableListOf())
            })
    }

    fun getVideoHistoryByPage(page: Int) {
        repo.loadVideoHistoryByPage(page)
            .map {
                val count = repo.getVideoHistoryCount()
                SettingsViewState(count, it)
            }.subscribeOn(RxSchedulers.io)
            .observeOn(RxSchedulers.ui)
            .autoDisposable(this)
            .subscribe({
                mViewState.value = it
            }, {
                mViewState.value = SettingsViewState(0, mutableListOf())
            })
    }

    fun deleteVideoHistoryAndUpdateData(videoCovers: MutableList<VideoCover>) {
        repo.deleteVideoHistory(videoCovers)
            .subscribeOn(RxSchedulers.io)
            .autoDisposable(this)
            .subscribe ({
                getVideoHistoryByPage(1)
            }, {
                getVideoHistoryByPage(1)
            })
    }

}