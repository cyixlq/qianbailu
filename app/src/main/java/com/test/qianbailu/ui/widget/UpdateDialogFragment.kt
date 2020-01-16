package com.test.qianbailu.ui.widget

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import com.test.qianbailu.R
import com.test.qianbailu.model.bean.UpdateAppBean
import kotlinx.android.synthetic.main.dialog_app_update.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.UpdateAppDialog)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_app_update, container)
    }

    override fun onStart() {
        super.onStart()
        dialog?.let { dialog ->
            dialog.window?.let { window ->
                window.setGravity(Gravity.CENTER)
                val lp = window.attributes
                val displayMetrics = context?.resources?.displayMetrics
                if (displayMetrics != null) {
                    lp.height = (displayMetrics.heightPixels * 0.7f).toInt()
                    lp.width = (displayMetrics.widthPixels * 0.8f).toInt()
                }
                window.attributes = lp
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            val updateAppBean: UpdateAppBean = it.getParcelable("bean") ?: UpdateAppBean()
            tv_title.text = "是否升级到${updateAppBean.version}版本？"
            tv_update_info.text = updateAppBean.desc
            if (updateAppBean.must) {
                line.visibility = View.GONE
                iv_close.visibility = View.GONE
            } else {
                iv_close.setOnClickListener {
                    dismiss()
                }
            }
            btn_ok.setOnClickListener {
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