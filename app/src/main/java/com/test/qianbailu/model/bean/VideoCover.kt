package com.test.qianbailu.model.bean

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  视频封面实体类
 */
@Entity(tableName = "history_video")
data class VideoCover(
    val name: String,
    val image: String,
    @PrimaryKey
    @ColumnInfo(name = "video_id")
    val videoId: String, // 例如：/video/4418/
    val position: Long = 0,
    val duration: Long = Long.MAX_VALUE
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeString(videoId)
        parcel.writeLong(position)
        parcel.writeLong(duration)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VideoCover> {
        override fun createFromParcel(parcel: Parcel): VideoCover {
            return VideoCover(parcel)
        }

        override fun newArray(size: Int): Array<VideoCover?> {
            return arrayOfNulls(size)
        }
    }
}