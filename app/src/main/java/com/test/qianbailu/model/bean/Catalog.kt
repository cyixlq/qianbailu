package com.test.qianbailu.model.bean

data class Catalog(
    val catalogUrl: String,
    val name: String,
    var isChecked: Boolean = false
)