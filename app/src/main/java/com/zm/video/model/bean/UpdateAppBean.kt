package com.zm.video.model.bean

import android.os.Parcel
import android.os.Parcelable

data class UpdateAppBean(
    var version: String = "",
    var code: Int = 0,
    var desc: String = "",
    var url: String = "",
    var must: Boolean = false
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(version)
        parcel.writeInt(code)
        parcel.writeString(desc)
        parcel.writeString(url)
        parcel.writeByte(if (must) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UpdateAppBean> {
        override fun createFromParcel(parcel: Parcel): UpdateAppBean {
            return UpdateAppBean(parcel)
        }

        override fun newArray(size: Int): Array<UpdateAppBean?> {
            return arrayOfNulls(size)
        }
    }
}