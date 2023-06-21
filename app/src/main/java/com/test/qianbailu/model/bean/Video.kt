package com.test.qianbailu.model.bean

data class Video(
    val name: String,
    val url: String,
    val cover: String,
    val downloadUrl: String,
    val parseType: Int,
    val position: Long = -1
)