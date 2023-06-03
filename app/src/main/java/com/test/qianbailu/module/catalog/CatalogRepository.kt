package com.test.qianbailu.module.catalog

import com.test.qianbailu.model.*
import com.test.qianbailu.model.bean.Catalog
import com.test.qianbailu.model.bean.Counter
import com.test.qianbailu.model.bean.IHtmlConverter
import com.test.qianbailu.model.bean.VideoCover
import io.reactivex.Observable

class CatalogDataSourceRepository(
    private val remote: CatalogRemoteDataSource
) {
    fun getAllCatalog(): Observable<Counter<Catalog>> {
        return remote.getAllCatalog()
    }

    fun getCatalogContent(catalogUrl: String, page: Int): Observable<Counter<VideoCover>> {
        return remote.getCatalogContent(catalogUrl, page)
    }

    fun getCatalogContentSync(catalogUrl: String, page: Int): Counter<VideoCover> {
        return remote.getCatalogContentSync(catalogUrl, page)
    }
}

class CatalogRemoteDataSource(private val api: ApiService, private val converter: IHtmlConverter) {

    fun getAllCatalog(): Observable<Counter<Catalog>> {
        return Observable.create {
            it.onNext(converter.getCatalogList())
            it.onComplete()
        }
    }

    fun getCatalogContentSync(catalogUrl: String, page: Int): Counter<VideoCover> {
        return converter.getVideoCoversByCatalog(catalogUrl, page)
    }

    fun getCatalogContent(catalogUrl: String, page: Int): Observable<Counter<VideoCover>> {
        return Observable.create {
            it.onNext(converter.getVideoCoversByCatalog(catalogUrl, page))
            it.onComplete()
        }
    }

}
