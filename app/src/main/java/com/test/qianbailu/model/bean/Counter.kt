package com.test.qianbailu.model.bean

data class Counter<T>(
    val count: Int,
    val children: MutableList<T>
)