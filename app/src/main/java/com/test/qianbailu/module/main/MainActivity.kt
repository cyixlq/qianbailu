package com.test.qianbailu.module.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import com.test.qianbailu.BuildConfig
import com.test.qianbailu.R
import com.test.qianbailu.databinding.ActivityMainBinding
import com.test.qianbailu.module.search.SearchActivity
import com.test.qianbailu.ui.adapter.ViewPagerFragmentAdapter
import com.test.qianbailu.ui.widget.UpdateDialogFragment
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import top.cyixlq.core.common.activity.CommonActivity
import top.cyixlq.core.utils.toastShort

class MainActivity : CommonActivity<ActivityMainBinding>() {

    private val mViewModel by viewModel<MainViewModel>()

    private var lastTime = 0L
    private val duration = 2000
    private var mShowTip = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        initView()
        binds()
        checkUpdate(false)
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
        fun launch(activity: FragmentActivity) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
        }
    }

    override val mViewBindingInflater: (inflater: LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate
}
