package com.test.qianbailu.model.bean

data class Video(
    val name: String,
    val url: String,
    val cover: String,
    val downloadUrl: String,
    val parseType: Int,
    // 猜你喜欢
    val likes: MutableList<VideoCover>?
)