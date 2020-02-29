package com.test.qianbailu.module.search

import androidx.lifecycle.MutableLiveData
import com.uber.autodispose.autoDisposable
import top.cyixlq.core.common.viewmodel.CommonViewModel
import top.cyixlq.core.utils.RxSchedulers

class SearchViewModel(
    private val repo: SearchDataSourceRepository,
    val mViewState: MutableLiveData<SearchViewState>
) : CommonViewModel() {

    fun searchVideo(keyword: String, page: Int) {
        repo.searchVideo(keyword, page)
            .doOnSubscribe { mViewState.postValue(SearchViewState(isLoading = true)) }
            .subscribeOn(RxSchedulers.io)
            .observeOn(RxSchedulers.ui)
            .autoDisposable(this)
            .subscribe({
                mViewState.postValue(SearchViewState(isLoading = false, counts = it))
            }, {
                mViewState.postValue(SearchViewState(isLoading = false, throwable = it))
            })
    }
}