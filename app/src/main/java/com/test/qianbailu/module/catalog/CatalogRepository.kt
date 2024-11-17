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

    fun getCatalogContent(catalog: Catalog, page: Int): Observable<Counter<VideoCover>> {
        return remote.getCatalogContent(catalog, page)
    }

    fun getCatalogContentSync(catalog: Catalog, page: Int): Counter<VideoCover> {
        return remote.getCatalogContentSync(catalog, page)
    }
}

class CatalogRemoteDataSource(private val api: ApiService, private val converter: IHtmlConverter) {

    fun getAllCatalog(): Observable<Counter<Catalog>> {
        return Observable.create {
            it.onNext(converter.getCatalogList())
            it.onComplete()
        }
    }

    fun getCatalogContentSync(catalog: Catalog, page: Int): Counter<VideoCover> {
        return converter.getVideoCoversByCatalog(catalog, page)
    }

    fun getCatalogContent(catalog: Catalog, page: Int): Observable<Counter<VideoCover>> {
        return Observable.create {
            it.onNext(converter.getVideoCoversByCatalog(catalog, page))
            it.onComplete()
        }
    }

}
