package com.test.qianbailu

import org.junit.Test

import org.junit.Assert.*
import java.util.regex.Pattern

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val str = "/class/有码-384.html"
        val p = Pattern.compile("[0-9]+(?=[^0-9]*$)")
        val m = p.matcher(str)
        if (m.find()) {
            println(m.group())
        } else {
            println("Not Found")
        }
    }
}
