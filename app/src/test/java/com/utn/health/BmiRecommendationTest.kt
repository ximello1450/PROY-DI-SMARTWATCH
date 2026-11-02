package com.utn.health

import com.utn.health.domain.model.BmiCategory
import com.utn.health.domain.model.BmiRecommendation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class BmiRecommendationTest {

    @Test
    fun testNormalBmi_calculatesHealthyRecommendation() {
        val rec = BmiRecommendation.calculate(70.0, 175.0)
        assertNotNull(rec)
        assertEquals(22.86, rec!!.bmi, 0.05)
        assertEquals(BmiCategory.NORMAL, rec.category)
        assertEquals(5.0, rec.recommendedDistanceKm, 0.01)
    }

    @Test
    fun testOverweightBmi_calculatesSafeFatBurnRecommendation() {
        val rec = BmiRecommendation.calculate(85.0, 175.0)
        assertNotNull(rec)
        assertEquals(27.76, rec!!.bmi, 0.05)
        assertEquals(BmiCategory.OVERWEIGHT, rec.category)
        assertEquals(3.5, rec.recommendedDistanceKm, 0.01)
    }

    @Test
    fun testUnderweightBmi_calculatesGentleRecommendation() {
        val rec = BmiRecommendation.calculate(48.0, 170.0)
        assertNotNull(rec)
        assertEquals(16.61, rec!!.bmi, 0.05)
        assertEquals(BmiCategory.UNDERWEIGHT, rec.category)
        assertEquals(2.5, rec.recommendedDistanceKm, 0.01)
    }

    @Test
    fun testObesityBmi_calculatesLowImpactRecommendation() {
        val rec = BmiRecommendation.calculate(105.0, 170.0)
        assertNotNull(rec)
        assertEquals(36.33, rec!!.bmi, 0.05)
        assertEquals(BmiCategory.OBESITY, rec.category)
        assertEquals(2.5, rec.recommendedDistanceKm, 0.01)
    }

    @Test
    fun testNullOrInvalidInputs_returnsNull() {
        assertNull(BmiRecommendation.calculate(null, 175.0))
        assertNull(BmiRecommendation.calculate(70.0, null))
        assertNull(BmiRecommendation.calculate(10.0, 175.0)) // peso fuera de rango
        assertNull(BmiRecommendation.calculate(70.0, 40.0))  // estatura fuera de rango
    }
}
