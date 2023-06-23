package com.test.qianbailu.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.qianbailu.model.bean.VideoCover
import com.test.qianbailu.model.dao.VideoHistoryDao

@Database(entities = [VideoCover::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun videoHistoryDao(): VideoHistoryDao
}