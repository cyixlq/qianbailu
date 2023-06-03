package com.test.qianbailu.ui.widget

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import com.test.qianbailu.R
import com.test.qianbailu.databinding.DialogAppUpdateBinding
import com.test.qianbailu.model.bean.UpdateAppBean
import top.cyixlq.core.utils.toastShort


class UpdateDialogFragment : DialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance(
            updateAppBean: UpdateAppBean
        ): UpdateDialogFragment {
            return UpdateDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("bean", updateAppBean)
                }
            }
        }
    }

    private lateinit var mBinding: DialogAppUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.UpdateAppDialog)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mBinding = DialogAppUpdateBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        val dialogWindow = dialog?.window
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER)
            val lp = dialogWindow.attributes
            val displayMetrics = context?.resources?.displayMetrics
            if (displayMetrics != null) {
                lp.height = (displayMetrics.heightPixels * 0.7f).toInt()
                lp.width = (displayMetrics.widthPixels * 0.8f).toInt()
            }
            dialogWindow.attributes = lp
        }
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            val updateAppBean: UpdateAppBean = it.getParcelable("bean") ?: UpdateAppBean()
            mBinding.tvTitle.text = "是否升级到${updateAppBean.version}版本？"
            mBinding.tvUpdateInfo.text = updateAppBean.desc
            if (updateAppBean.must) {
                mBinding.line.visibility = View.GONE
                mBinding.ivClose.visibility = View.GONE
            } else {
                mBinding.ivClose.setOnClickListener {
                    dismiss()
                }
            }
            mBinding.btnOk.setOnClickListener {
                if (updateAppBean.url.startsWith("http")) {
                    startActivity(Intent(Intent.ACTION_VIEW, updateAppBean.url.toUri()))
                    "正在跳转到浏览器下载".toastShort()
                } else {
                    "链接有误".toastShort()
                }
            }
        }
    }
}