package com.test.qianbailu.module.search

import com.test.qianbailu.model.bean.SearchHistory

data class SearchViewState(
    val histories: MutableList<SearchHistory>? = null
)