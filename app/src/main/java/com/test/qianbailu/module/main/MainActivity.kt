package com.test.qianbailu.module.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.test.qianbailu.BuildConfig
import com.test.qianbailu.R
import com.test.qianbailu.databinding.ActivityMainBinding
import com.test.qianbailu.module.search.SearchActivity
import com.test.qianbailu.ui.adapter.ViewPagerFragmentAdapter
import com.test.qianbailu.ui.widget.UpdateDialogFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import top.cyixlq.core.common.activity.CommonActivity
import top.cyixlq.core.utils.CLog
import top.cyixlq.core.utils.toastShort

class MainActivity : CommonActivity<ActivityMainBinding>() {

    private val mViewModel by viewModel<MainViewModel>()

    private var lastTime = 0L
    private val duration = 2000
    private var mShowTip = false
    private var mIsReady = false
    private var mJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        initView()
        binds()
        checkUpdate(false)
        mJob = lifecycleScope.launch {
            delay(2000)
            mIsReady = true
            CLog.t(TAG).d("lifecycleScope ready......")
        }
        // Set up an OnPreDrawListener to the root view.
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check whether the initial data is ready.
                    return if (mIsReady) {
                        // The content is ready. Start drawing.
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        // The content isn't ready. Suspend.
                        false
                    }
                }
            }
        )
    }

    fun isReady() = mIsReady

    fun ready() {
        mIsReady = true
        mJob?.cancel()
    }

    fun checkUpdate(showTip: Boolean) {
        if (mShowTip != showTip) {
            mShowTip = showTip
        }
        mViewModel.getVersionInfo()
    }

    private fun binds() {
        mViewModel.viewState.observe(this) {
            if (it.updateAppBean != null) {
                if (BuildConfig.VERSION_CODE < it.updateAppBean.code) {
                    UpdateDialogFragment
                        .newInstance(it.updateAppBean)
                        .show(supportFragmentManager, "tag")
                } else if (mShowTip) {
                    getString(R.string.no_updated_version).toastShort()
                }
            }
        }
    }

    private fun initView() {
        val adapter: ViewPagerFragmentAdapter = get { parametersOf(this) }
        mBinding.vpMain.adapter = adapter
        mBinding.vpMain.isUserInputEnabled = false
        mBinding.btmNav.setOnNavigationItemSelectedListener {
            val index = when(it.itemId) {
                R.id.menuCatalog -> 1
                R.id.menuSettings -> 2
                else -> 0
            }
            mBinding.topBar.setTitle(getTitleString(index))
            mBinding.vpMain.setCurrentItem(index, false)
            return@setOnNavigationItemSelectedListener true
        }
        mBinding.topBar.setTitle(getTitleString(mBinding.vpMain.currentItem))
        mBinding.topBar.setRightImgClickListener {
            SearchActivity.launch(this)
        }
    }

    private fun getTitleString(index: Int) =
        when (index) {
            1 -> getString(R.string.category)
            2 -> getString(R.string.settings)
            else -> getString(R.string.home_page)
        }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if ((currentTime - lastTime) > duration) {
            getString(R.string.press_again_to_exit).toastShort()
            lastTime = currentTime
        } else {
            @Suppress("DEPRECATION")
            super.onBackPressed()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }

    override val mViewBindingInflater: (inflater: LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate
}
