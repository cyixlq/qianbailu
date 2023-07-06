package com.test.qianbailu.model.dao

import androidx.room.*
import com.test.qianbailu.model.PAGE_COUNT
import com.test.qianbailu.model.bean.VideoCover
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface VideoHistoryDao {

    @Query("SELECT * from history_video order by rowid desc limit $PAGE_COUNT offset 0")
    fun observeFirstPage(): Observable<MutableList<VideoCover>>

    @Query("SELECT * from history_video order by rowid desc limit $PAGE_COUNT offset :page")
    fun loadByPage(page: Int): Single<MutableList<VideoCover>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(videoCover: VideoCover): Completable

    @Query("SELECT * from history_video WHERE video_id = :videoId")
    fun findItem(videoId: String): Single<VideoCover?>

    @Query("SELECT COUNT(*) FROM history_video")
    fun getCount(): Int

    @Delete
    fun deleteSome(videoCovers: MutableList<VideoCover>): Completable

}