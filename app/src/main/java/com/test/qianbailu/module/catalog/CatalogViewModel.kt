package com.test.qianbailu.module.catalog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uber.autodispose.autoDisposable
import top.cyixlq.core.common.viewmodel.CommonViewModel
import top.cyixlq.core.utils.RxSchedulers

class CatalogViewModel(private val repo: CatalogDataSourceRepository) : CommonViewModel() {

    val mViewState = MutableLiveData<CatalogViewState>()

    fun getAllCatalog() {
        repo.getAllCatalog()
            .doOnSubscribe { mViewState.postValue(CatalogViewState(isLoading = true)) }
            .subscribeOn(RxSchedulers.io)
            .observeOn(RxSchedulers.ui)
            .autoDisposable(this)
            .subscribe({
                mViewState.postValue(CatalogViewState(isLoading = false, allCatalog = it))
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

@Suppress("UNCHECKED_CAST")
class CatalogViewModelFactory(private val repo: CatalogDataSourceRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CatalogViewModel(repo) as T
    }
}