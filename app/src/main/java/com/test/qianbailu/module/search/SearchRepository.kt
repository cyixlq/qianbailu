package com.test.qianbailu.module.search

import com.test.qianbailu.model.AppDatabase
import com.test.qianbailu.model.bean.Counter
import com.test.qianbailu.model.bean.IHtmlConverter
import com.test.qianbailu.model.bean.SearchHistory
import com.test.qianbailu.model.bean.VideoCover
import com.test.qianbailu.model.dao.SearchHistoryDao
import io.reactivex.Observable
import top.cyixlq.core.utils.RxSchedulers

class SearchDataSourceRepository(
    private val remote: SearchRemoteDataSource,
    private val local: SearchLocalDataSource
) {
    fun searchVideo(keyword: String, page: Int): Observable<Counter<VideoCover>> {
        return remote.searchVideo(keyword, page)
    }

    fun observeAllHistory() = local.observeAllHistory()

    fun insertItemHistory(searchHistory: SearchHistory) = local.insertItemHistory(searchHistory)

    fun deleteItemHistory(searchHistories: MutableList<SearchHistory>) = local.deleteItemHistory(searchHistories)
}

class SearchRemoteDataSource(private val converter: IHtmlConverter) {
    fun searchVideo(keyword: String, page: Int): Observable<Counter<VideoCover>> {
        return Observable.create {
            it.onNext(converter.search(keyword, page))
            it.onComplete()
        }
    }
}

class SearchLocalDataSource(private val appDatabase: AppDatabase) {
    fun observeAllHistory(): Observable<MutableList<SearchHistory>> =
        appDatabase.searchHistoryDao().observeAll()

    fun insertItemHistory(searchHistory: SearchHistory) =
        appDatabase.searchHistoryDao().insertItem(searchHistory)

    fun deleteItemHistory(searchHistories: MutableList<SearchHistory>) =
        appDatabase.searchHistoryDao().deleteItem(searchHistories)
}