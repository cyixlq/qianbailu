package com.test.qianbailu.module.catalog

import com.test.qianbailu.model.bean.AllCatalog
import com.test.qianbailu.model.bean.Counts
import com.test.qianbailu.model.bean.VideoCover

data class CatalogViewState(
    val isLoading: Boolean = false,
    val throwable: Throwable? = null,
    val allCatalog: AllCatalog? = null,
    val videoCovers: Counts<VideoCover>? = null
)