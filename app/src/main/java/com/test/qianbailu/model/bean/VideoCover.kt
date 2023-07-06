package com.test.qianbailu.model.bean

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 *  视频封面实体类
 */
@Entity(tableName = "history_video")
@Parcelize
data class VideoCover(
    val name: String,
    val image: String,
    @PrimaryKey
    @ColumnInfo(name = "video_id")
    val videoId: String, // 例如：/video/4418/
    val position: Long = 0,
    val duration: Long = Long.MAX_VALUE
): Parcelable