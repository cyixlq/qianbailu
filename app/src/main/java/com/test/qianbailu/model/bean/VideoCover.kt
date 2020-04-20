package com.test.qianbailu.model.bean

import android.os.Parcel
import android.os.Parcelable

/**
 *  视频封面实体类
 */
data class VideoCover(
    var name: String,
    var image: String,
    var videoId: String, // 例如：/video/4418/
    var duration: String,
    var viewCount: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeString(videoId)
        parcel.writeString(duration)
        parcel.writeString(viewCount)
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