package com.test.qianbailu.model.dao

import androidx.room.*
import com.test.qianbailu.model.bean.SearchHistory
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface SearchHistoryDao {

    @Query("SELECT * from search_history order by rowid desc")
    fun observeAll(): Observable<MutableList<SearchHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(searchHistory: SearchHistory): Completable

    @Delete
    fun deleteItem(searchHistories: MutableList<SearchHistory>): Completable
}