package com.myapp.tipcalculator

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun calculateTipPercent_20percent() {
        val amount = 10.0
        val tipPercent = 20.0
        val actualTip = calculateTip(amount = amount, tipPercent = tipPercent, roundUp = false)
        val expectedtip = 2.0
        assertEquals(expectedtip, actualTip)
    }
}