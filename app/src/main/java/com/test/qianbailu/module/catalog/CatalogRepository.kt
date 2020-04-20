package com.test.qianbailu.module.catalog

import com.test.qianbailu.model.*
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
        return Observable.just(HOST)
            .flatMap(FlatmapOrError(api.getAllCatalogHtml(HOST + ALL_CATALOG_URL)))
            .retryWhen(NoHostRetry(api, api.getAllCatalogHtml(HOST + ALL_CATALOG_URL)))
            .map {
                val html = it.string()
                val videoCovers = html.html2VideoCoverList()
                val catalogs = html.html2CatalogList()
                AllCatalog(catalogs, videoCovers)
            }
    }

    fun getCatalogContent(catalogUrl: String, page: Int): Observable<Counts<VideoCover>> {
        return Observable.just(HOST)
            .flatMap(FlatmapOrError(api.getCatalogHtml(HOST + catalogUrl, page)))
            .retryWhen(NoHostRetry(api, api.getCatalogHtml(HOST + catalogUrl, page)))
            .map {
                val html = it.string()
                html.html2VideoCoverCounts()
            }
    }

}
