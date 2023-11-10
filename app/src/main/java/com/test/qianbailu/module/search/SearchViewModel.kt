package com.test.qianbailu.module.search

import androidx.lifecycle.MutableLiveData
import com.test.qianbailu.model.bean.SearchHistory
import com.uber.autodispose.autoDisposable
import top.cyixlq.core.common.viewmodel.CommonViewModel
import top.cyixlq.core.utils.RxSchedulers

class SearchViewModel(
    private val repo: SearchDataSourceRepository,
    val mSearchHistoryViewState: MutableLiveData<SearchViewState>,
    val mSearchResultViewState: MutableLiveData<SearchResultViewState>
) : CommonViewModel() {

    fun searchVideo(keyword: String, page: Int) {
        repo.searchVideo(keyword, page)
            .doOnSubscribe { mSearchResultViewState.postValue(SearchResultViewState(isLoading = true)) }
            .subscribeOn(RxSchedulers.io)
            .observeOn(RxSchedulers.ui)
            .autoDisposable(this)
            .subscribe({
                mSearchResultViewState.postValue(SearchResultViewState(isLoading = false, counts = it))
            }, {
                mSearchResultViewState.postValue(SearchResultViewState(isLoading = false, throwable = it))
            })
    }

    fun observeAllHistory() {
        repo.observeAllHistory()
            .subscribeOn(RxSchedulers.io)
            .observeOn(RxSchedulers.ui)
            .autoDisposable(this)
            .subscribe {
                mSearchHistoryViewState.value = SearchViewState(histories = it)
            }
    }

    fun insertHistory(searchHistory: SearchHistory) {
        repo.insertItemHistory(searchHistory)
            .subscribeOn(RxSchedulers.io)
            .observeOn(RxSchedulers.ui)
            .autoDisposable(this)
            .subscribe()
    }

    fun deleteHistory(searchHistories: MutableList<SearchHistory>) {
        repo.deleteItemHistory(searchHistories)
            .subscribeOn(RxSchedulers.io)
            .observeOn(RxSchedulers.ui)
            .autoDisposable(this)
            .subscribe()
    }
}