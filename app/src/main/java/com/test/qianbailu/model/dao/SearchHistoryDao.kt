package com.test.qianbailu.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.qianbailu.model.bean.SearchHistory
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface SearchHistoryDao {

    @Query("SELECT * from search_history order by rowid desc")
    fun observeAll(): Observable<MutableList<SearchHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(searchHistory: SearchHistory): Completable

    @Delete
    fun deleteItem(searchHistories: MutableList<SearchHistory>): Completable
}