package com.test.qianbailu.model.bean

import com.google.gson.annotations.SerializedName
import java.net.URLDecoder

data class LiveRooms(
    @SerializedName("zhubo")
    val liveRoomList: MutableList<LiveRoom>
)

data class LiveRoom(
    val address: String,
    val img: String
) {
    val title: String = ""
        get() {return URLDecoder.decode(field.replace("+", "%20"), "utf-8")}
}