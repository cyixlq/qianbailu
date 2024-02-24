package com.test.qianbailu

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.test.qianbailu.model.ApiService
import com.test.qianbailu.model.AppDatabase
import com.test.qianbailu.module.catalog.CatalogDataSourceRepository
import com.test.qianbailu.module.catalog.CatalogFragment
import com.test.qianbailu.module.catalog.CatalogRemoteDataSource
import com.test.qianbailu.module.catalog.CatalogViewModel
import com.test.qianbailu.module.catalog.CatalogViewState
import com.test.qianbailu.module.home.HomeDataSourceRepository
import com.test.qianbailu.module.home.HomeFragment
import com.test.qianbailu.module.home.HomeRemoteDataSource
import com.test.qianbailu.module.home.HomeViewModel
import com.test.qianbailu.module.home.HomeViewState
import com.test.qianbailu.module.live.AllPlatformDataSourceRepository
import com.test.qianbailu.module.live.AllPlatformFragment
import com.test.qianbailu.module.live.AllPlatformRemoteDataSource
import com.test.qianbailu.module.live.AllPlatformViewModel
import com.test.qianbailu.module.live.AllPlatformViewState
import com.test.qianbailu.module.live.room.LiveRoomsActivity
import com.test.qianbailu.module.live.room.LiveRoomsDataSourceRepository
import com.test.qianbailu.module.live.room.LiveRoomsRemoteDataSource
import com.test.qianbailu.module.live.room.LiveRoomsViewModel
import com.test.qianbailu.module.live.room.LiveRoomsViewState
import com.test.qianbailu.module.main.MainActivity
import com.test.qianbailu.module.main.MainDataSourceRepository
import com.test.qianbailu.module.main.MainRemoteDataSource
import com.test.qianbailu.module.main.MainViewModel
import com.test.qianbailu.module.main.MainViewState
import com.test.qianbailu.module.search.SearchActivity
import com.test.qianbailu.module.search.SearchDataSourceRepository
import com.test.qianbailu.module.search.SearchLocalDataSource
import com.test.qianbailu.module.search.SearchRemoteDataSource
import com.test.qianbailu.module.search.SearchResultActivity
import com.test.qianbailu.module.search.SearchResultViewState
import com.test.qianbailu.module.search.SearchViewModel
import com.test.qianbailu.module.search.SearchViewState
import com.test.qianbailu.module.settings.SettingsFragment
import com.test.qianbailu.module.settings.SettingsLocalSource
import com.test.qianbailu.module.settings.SettingsRepository
import com.test.qianbailu.module.settings.SettingsViewModel
import com.test.qianbailu.module.settings.SettingsViewState
import com.test.qianbailu.module.video.VideoActivity
import com.test.qianbailu.module.video.VideoDataSourceRepository
import com.test.qianbailu.module.video.VideoLocalDataSource
import com.test.qianbailu.module.video.VideoRemoteDataSource
import com.test.qianbailu.module.video.VideoViewModel
import com.test.qianbailu.module.video.VideoViewState
import com.test.qianbailu.ui.adapter.LivePlatformAdapter
import com.test.qianbailu.ui.adapter.LiveRoomAdapter
import com.test.qianbailu.ui.adapter.VideoCoverAdapter
import com.test.qianbailu.ui.adapter.ViewPagerFragmentAdapter
import com.test.qianbailu.utils.HtmlConverterFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import top.cyixlq.core.net.RetrofitManager
import top.cyixlq.core.utils.FormatUtil

val httpModule = module {

    single {
        RetrofitManager.getInstance().create(ApiService::class.java)
    }

    single {
        FormatUtil.getGson()
    }

    single {
        HtmlConverterFactory.get(get())
    }

    single {
        val migration1To2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `search_history` (`keyword` TEXT NOT NULL, PRIMARY KEY(`keyword`))")
            }
        }
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "app-database"
        ).addMigrations(migration1To2).fallbackToDestructiveMigration().build()
    }
}

val mvvmModule = module {

    // Common Adapter
    factory { VideoCoverAdapter() }

    // Main
    scope<MainActivity> {
        factory { MainRemoteDataSource(get()) }
        factory { MainDataSourceRepository(get()) }
        factory { MutableLiveData<MainViewState>() }
        scoped { (activity: FragmentActivity) ->
            ViewPagerFragmentAdapter(
                activity,
                arrayListOf(
                    HomeFragment.instance(),
                    CatalogFragment.instance(),
                    SettingsFragment.instance())
            )
        }
        viewModel { MainViewModel(get(), get()) }
    }

    // Home
    scope<HomeFragment> {
        factory { HomeRemoteDataSource(get()) }
        factory { HomeDataSourceRepository(get()) }
        factory { MutableLiveData<HomeViewState>() }
        viewModel { HomeViewModel(get(), get()) }
    }

    // Catalog
    scope<CatalogFragment> {
        factory { CatalogRemoteDataSource(get(), get()) }
        factory { CatalogDataSourceRepository(get()) }
        factory { MutableLiveData<CatalogViewState>() }
        viewModel { CatalogViewModel(get(), get()) }
    }

    // Search
    scope<SearchActivity> {
        factory { SearchLocalDataSource(get()) }
        factory { SearchRemoteDataSource(get()) }
        factory { SearchDataSourceRepository(get(), get()) }
        factory { MutableLiveData<SearchViewState>() }
        factory { MutableLiveData<SearchResultViewState>() }
        viewModel { SearchViewModel(get(), get(), get()) }
    }

    // Search Result
    scope<SearchResultActivity> {
        factory { SearchLocalDataSource(get()) }
        factory { SearchRemoteDataSource(get()) }
        factory { SearchDataSourceRepository(get(), get()) }
        factory { MutableLiveData<SearchViewState>() }
        factory { MutableLiveData<SearchResultViewState>() }
        viewModel { SearchViewModel(get(), get(), get()) }
    }

    // Video
    scope<VideoActivity> {
        factory { VideoRemoteDataSource(get()) }
        factory { VideoLocalDataSource(get()) }
        factory { VideoDataSourceRepository(get(), get()) }
        factory { MutableLiveData<VideoViewState>() }
        viewModel { VideoViewModel(get(), get()) }
    }

    // Platforms
    scope<AllPlatformFragment> {
        factory { AllPlatformRemoteDataSource(get()) }
        factory { AllPlatformDataSourceRepository(get()) }
        factory { MutableLiveData<AllPlatformViewState>() }
        factory { LivePlatformAdapter() }
        viewModel { AllPlatformViewModel(get(), get()) }
    }

    // LiveRooms
    scope<LiveRoomsActivity> {
        factory { LiveRoomsRemoteDataSource(get()) }
        factory { LiveRoomsDataSourceRepository(get()) }
        factory { MutableLiveData<LiveRoomsViewState>() }
        factory { LiveRoomAdapter() }
        viewModel { LiveRoomsViewModel(get(), get()) }
    }

    // Settings VideoHistory
    factory { SettingsLocalSource(get()) }
    factory { SettingsRepository(get()) }
    factory { MutableLiveData<SettingsViewState>() }
    viewModel { SettingsViewModel(get(), get()) }
}