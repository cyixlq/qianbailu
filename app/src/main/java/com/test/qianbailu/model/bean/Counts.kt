package com.test.qianbailu.model.bean

data class Counts<T>(
    val count: Int,
    val list: MutableList<T>
)