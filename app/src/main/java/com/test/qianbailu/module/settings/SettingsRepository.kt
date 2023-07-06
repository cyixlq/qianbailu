package com.test.qianbailu.module.settings

import com.test.qianbailu.model.AppDatabase
import com.test.qianbailu.model.PAGE_COUNT
import com.test.qianbailu.model.bean.Counter
import com.test.qianbailu.model.bean.IHtmlConverter
import com.test.qianbailu.model.bean.VideoCover
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import top.cyixlq.core.utils.RxSchedulers

class SettingsRepository(
    private val local: SettingsLocalSource
) {
    fun observeVideoHistoryFirstPage() = local.observeVideoHistoryFirstPage()
    fun getVideoHistoryCount(): Int = local.getVideoHistoryCount()
    fun loadVideoHistoryByPage(page: Int) = local.loadVideoHistoryByPage(page)
    fun deleteVideoHistory(videoCovers: MutableList<VideoCover>) = local.deleteVideoHistory(videoCovers)
}

class SettingsLocalSource(private val appDatabase: AppDatabase) {

    fun observeVideoHistoryFirstPage(): Observable<MutableList<VideoCover>> {
        return appDatabase.videoHistoryDao().observeFirstPage()
    }

    fun loadVideoHistoryByPage(page: Int): Single<MutableList<VideoCover>> {
        return appDatabase.videoHistoryDao().loadByPage((page - 1) * PAGE_COUNT)
    }

    fun getVideoHistoryCount(): Int {
        return appDatabase.videoHistoryDao().getCount()
    }

    fun deleteVideoHistory(videoCovers: MutableList<VideoCover>): Completable {
        return appDatabase.videoHistoryDao().deleteSome(videoCovers)
    }
}