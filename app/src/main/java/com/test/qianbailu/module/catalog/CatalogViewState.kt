package com.test.qianbailu.module.catalog

import com.test.qianbailu.model.bean.Catalog
import com.test.qianbailu.model.bean.Counter
import com.test.qianbailu.model.bean.VideoCover

data class CatalogViewState(
    val isLoading: Boolean = false,
    val throwable: Throwable? = null,
    val allCatalog: Counter<Catalog>? = null,
    val videoCovers: Counter<VideoCover>? = null
)