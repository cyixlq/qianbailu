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

        val input = "\"id\": \"player-con\", \"source\": \"https://ccc3.ddn2024ddb6666.com/cdn2024/202407/06/6687b5b17327970b087ebb19/e9f0a4/index.m3u8\", \"width\": \"100%\","
        val regex = "\"source\": \"(.*?)\", \""
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(input)
        if (matcher.find()) {
            println(matcher.group(1))
        } else {
            println("Not Found")
        }
    }
}
