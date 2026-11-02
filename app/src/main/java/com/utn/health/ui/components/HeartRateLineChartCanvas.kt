package com.utn.health.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.utn.health.domain.model.HeartRateSample
import com.utn.health.ui.theme.HealthHeartRateRed

@Composable
fun HeartRateLineChartCanvas(
    samples: List<HeartRateSample>,
    modifier: Modifier = Modifier
) {
    if (samples.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sin muestras de frecuencia cardíaca hoy",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
        return
    }

    val minBpm = (samples.minOfOrNull { it.bpm } ?: 50L).toFloat()
    val maxBpm = (samples.maxOfOrNull { it.bpm } ?: 150L).toFloat().coerceAtLeast(minBpm + 10f)

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
    ) {
        val width = size.width
        val height = size.height - 30.dp.toPx()
        val bpmRange = maxBpm - minBpm

        // Líneas guía horizontales (Mínimo, Medio, Máximo)
        val guideYValues = listOf(0.1f, 0.5f, 0.9f)
        guideYValues.forEach { ratio ->
            val y = height * ratio
            drawLine(
                color = Color.LightGray.copy(alpha = 0.35f),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1.dp.toPx()
            )
        }

        val stepX = width / (samples.size - 1).coerceAtLeast(1)
        val strokePath = Path()
        val fillPath = Path()

        samples.forEachIndexed { index, sample ->
            val x = index * stepX
            val normalizedBpm = (sample.bpm.toFloat() - minBpm) / bpmRange
            val y = height - (normalizedBpm * (height - 20.dp.toPx()))

            if (index == 0) {
                strokePath.moveTo(x, y)
                fillPath.moveTo(x, height)
                fillPath.lineTo(x, y)
            } else {
                strokePath.lineTo(x, y)
                fillPath.lineTo(x, y)
            }

            if (index == samples.size - 1) {
                fillPath.lineTo(x, height)
                fillPath.close()
            }
        }

        // Dibujar gradiente de relleno inferior
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    HealthHeartRateRed.copy(alpha = 0.35f),
                    HealthHeartRateRed.copy(alpha = 0.02f)
                ),
                startY = 0f,
                endY = height
            )
        )

        // Dibujar trazo principal de la línea
        drawPath(
            path = strokePath,
            color = HealthHeartRateRed,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}
