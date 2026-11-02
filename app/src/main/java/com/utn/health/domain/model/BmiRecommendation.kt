package com.utn.health.domain.model

import java.util.Locale

enum class BmiCategory(val displayName: String, val description: String) {
    UNDERWEIGHT(
        "Bajo peso",
        "Se recomienda iniciar con actividad suave para desarrollar acondicionamiento físico sin excesivo desgaste calórico."
    ),
    NORMAL(
        "Peso saludable",
        "Meta activa recomendada por la OMS (6,000 - 8,000 pasos diarios) para mantener una óptima salud cardiovascular."
    ),
    OVERWEIGHT(
        "Sobrepeso",
        "Caminata moderada constante para favorecer la quema de grasa y déficit calórico seguro, minimizando el impacto articular."
    ),
    OBESITY(
        "Obesidad",
        "Actividad de bajo impacto progresivo para cuidar rodillas y articulaciones, priorizando la constancia diaria antes que la velocidad."
    )
}

data class BmiRecommendation(
    val weightKg: Double,
    val heightCm: Double,
    val bmi: Double,
    val category: BmiCategory,
    val recommendedDistanceKm: Double,
    val explanation: String
) {
    val bmiFormatted: String
        get() = String.format(Locale.getDefault(), "%.1f", bmi)

    companion object {
        fun calculate(weightKg: Double?, heightCm: Double?): BmiRecommendation? {
            if (weightKg == null || heightCm == null) return null
            if (weightKg !in 25.0..250.0 || heightCm !in 80.0..230.0) return null

            val heightM = heightCm / 100.0
            val bmi = weightKg / (heightM * heightM)

            val (category, recDistance, explanation) = when {
                bmi < 18.5 -> Triple(
                    BmiCategory.UNDERWEIGHT,
                    2.5,
                    "Tu IMC es de ${String.format(Locale.getDefault(), "%.1f", bmi)} (Bajo peso). Te recomendamos una meta de 2.5 km diarios para acondicionamiento físico paulatino."
                )
                bmi < 25.0 -> Triple(
                    BmiCategory.NORMAL,
                    5.0,
                    "Tu IMC es de ${String.format(Locale.getDefault(), "%.1f", bmi)} (Saludable). Una meta de 5.0 km al día es óptima para mantener tu capacidad cardiovascular."
                )
                bmi < 30.0 -> Triple(
                    BmiCategory.OVERWEIGHT,
                    3.5,
                    "Tu IMC es de ${String.format(Locale.getDefault(), "%.1f", bmi)} (Sobrepeso). Una meta diaria de 3.5 km favorece el déficit calórico protegiendo tus articulaciones."
                )
                else -> Triple(
                    BmiCategory.OBESITY,
                    2.5,
                    "Tu IMC es de ${String.format(Locale.getDefault(), "%.1f", bmi)} (Obesidad). Se sugiere una meta progresiva de 2.5 km a ritmo cómodo para cuidar rodillas y tobillos."
                )
            }

            return BmiRecommendation(
                weightKg = weightKg,
                heightCm = heightCm,
                bmi = bmi,
                category = category,
                recommendedDistanceKm = recDistance,
                explanation = explanation
            )
        }
    }
}
