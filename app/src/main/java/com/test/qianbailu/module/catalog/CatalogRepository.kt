package com.test.qianbailu.module.catalog

import com.test.qianbailu.model.ApiService
import com.test.qianbailu.model.bean.AllCatalog
import com.test.qianbailu.model.bean.Counts
import com.test.qianbailu.model.bean.VideoCover
import com.test.qianbailu.utils.html2CatalogList
import com.test.qianbailu.utils.html2VideoCoverCounts
import com.test.qianbailu.utils.html2VideoCoverList
import io.reactivex.Observable

class CatalogDataSourceRepository(
    private val remote: CatalogRemoteDataSource
) {
    fun getAllCatalog(): Observable<AllCatalog> {
        return remote.getAllCatalog()
    }

    fun getCatalogContent(catalogUrl: String, page: Int): Observable<Counts<VideoCover>> {
        return remote.getCatalogContent(catalogUrl, page)
    }
}

class CatalogRemoteDataSource(private val api: ApiService) {

    fun getAllCatalog(): Observable<AllCatalog> {
        return api.getAllCatalogHtml()
            .map { it.string() }
            .map {
                val videoCovers = it.html2VideoCoverList()
                val catalogs = it.html2CatalogList()
                AllCatalog(catalogs, videoCovers)
            }
    }

    fun getCatalogContent(catalogUrl: String, page: Int): Observable<Counts<VideoCover>> {
        return api.getCatalogHtml(catalogUrl, page)
            .map { it.string() }
            .map {
                it.html2VideoCoverCounts()
            }
    }

}
