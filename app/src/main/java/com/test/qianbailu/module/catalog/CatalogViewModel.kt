package com.test.qianbailu.module.catalog

import androidx.lifecycle.MutableLiveData
import com.uber.autodispose.autoDisposable
import io.reactivex.Observable
import top.cyixlq.core.common.viewmodel.CommonViewModel
import top.cyixlq.core.utils.RxSchedulers

class CatalogViewModel(
    private val repo: CatalogDataSourceRepository,
    val mViewState: MutableLiveData<CatalogViewState>
) : CommonViewModel() {

    fun getAllCatalog() {
        repo.getAllCatalog()
            .concatMap {
                val catalogUrl = it.children.first().catalogUrl
                val videos = repo.getCatalogContentSync(catalogUrl, 1)
                return@concatMap Observable.create<CatalogViewState> { emitter ->
                    emitter.onNext(CatalogViewState(allCatalog = it, videoCovers = videos))
                    emitter.onComplete()
                }
            }
            .doOnSubscribe { mViewState.postValue(CatalogViewState(isLoading = true)) }
            .subscribeOn(RxSchedulers.io)
            .observeOn(RxSchedulers.ui)
            .autoDisposable(this)
            .subscribe({
                mViewState.postValue(it)
            }, {
                mViewState.postValue(CatalogViewState(isLoading = false, throwable = it))
            })
    }

    fun getCatalogContent(catalogUrl: String, page: Int) {
        repo.getCatalogContent(catalogUrl, page)
            .doOnSubscribe { mViewState.postValue(CatalogViewState(isLoading = true)) }
            .subscribeOn(RxSchedulers.io)
            .observeOn(RxSchedulers.ui)
            .autoDisposable(this)
            .subscribe({
                mViewState.postValue(CatalogViewState(isLoading = false, videoCovers = it))
            }, {
                mViewState.postValue(CatalogViewState(isLoading = false, throwable = it))
            })
    }
}