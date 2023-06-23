package com.test.qianbailu.model.dao

import androidx.room.*
import com.test.qianbailu.model.bean.VideoCover
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface VideoHistoryDao {

    @Query("SELECT * from history_video order by rowid desc")
    fun loadAll(): Observable<MutableList<VideoCover>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(videoCover: VideoCover): Completable

    @Query("SELECT * from history_video WHERE video_id = :videoId")
    fun findItem(videoId: String): Single<VideoCover?>

}