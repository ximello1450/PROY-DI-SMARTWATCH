package com.utn.health.domain.model

enum class MetricType(val displayName: String, val unit: String) {
    STEPS("Pasos", "pasos"),
    DISTANCE("Distancia", "km"),
    CALORIES("Calorías", "kcal"),
    HEART_RATE_AVG("Frecuencia Cardíaca", "lpm"),
    SLEEP("Sueño", "horas")
}

enum class SummaryMetric {
    STEPS,
    DISTANCE,
    CALORIES,
    HEART_RATE,
    SLEEP
}
