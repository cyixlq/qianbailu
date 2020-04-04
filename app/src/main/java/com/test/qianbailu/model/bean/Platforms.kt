package com.test.qianbailu.model.bean

import com.google.gson.annotations.SerializedName

data class Platforms(
    @SerializedName("pingtai")
    val platformList: MutableList<Platform>
)

data class Platform(
    @SerializedName("Number")
    val number: String,
    val address: String,
    val title: String,
    @SerializedName("xinimg")
    val image: String
)