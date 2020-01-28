package com.test.qianbailu.model.bean

data class AllCatalog(
    val catalogs: MutableList<Catalog>,
    val videocCovers: MutableList<VideoCover>
)