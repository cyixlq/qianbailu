package top.cyixlq.core.utils

import android.widget.Toast
import androidx.annotation.StringRes
import top.cyixlq.core.CoreManager

object ToastUtil {
    fun showShort(text: String) {
        Toast.makeText(CoreManager.getApplication(), text, Toast.LENGTH_SHORT).show()
    }

    fun showShort(@StringRes stringId: Int) {
        Toast.makeText(CoreManager.getApplication(), stringId, Toast.LENGTH_SHORT).show()
    }

    fun showLong(@StringRes stringId: Int) {
        Toast.makeText(CoreManager.getApplication(), stringId, Toast.LENGTH_LONG).show()
    }

    fun showLong(text: String) {
        Toast.makeText(CoreManager.getApplication(), text, Toast.LENGTH_LONG).show()
    }
}