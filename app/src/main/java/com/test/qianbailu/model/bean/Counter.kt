package com.test.qianbailu.model.bean

data class Counter<T>(
    val totalPage: Int,
    val children: MutableList<T>
)