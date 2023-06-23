package com.test.qianbailu.module.settings

import com.test.qianbailu.model.AppDatabase
import com.test.qianbailu.model.bean.Counter
import com.test.qianbailu.model.bean.IHtmlConverter
import com.test.qianbailu.model.bean.VideoCover
import io.reactivex.Observable
import top.cyixlq.core.utils.RxSchedulers

class SettingsRepository(
    private val local: SettingsLocalSource
) {
    fun getAllVideoHistory() = local.getAllVideoHistory()
}

class SettingsLocalSource(private val appDatabase: AppDatabase) {

    fun getAllVideoHistory(): Observable<MutableList<VideoCover>> {
        return appDatabase.videoHistoryDao().loadAll()
    }
}