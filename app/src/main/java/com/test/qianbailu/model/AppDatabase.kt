package com.test.qianbailu.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.qianbailu.model.bean.SearchHistory
import com.test.qianbailu.model.bean.VideoCover
import com.test.qianbailu.model.dao.SearchHistoryDao
import com.test.qianbailu.model.dao.VideoHistoryDao

@Database(entities = [VideoCover::class, SearchHistory::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun videoHistoryDao(): VideoHistoryDao
    abstract fun searchHistoryDao(): SearchHistoryDao
}