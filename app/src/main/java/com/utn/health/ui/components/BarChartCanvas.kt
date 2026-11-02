package com.utn.health.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.utn.health.domain.model.HistoryPoint
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun BarChartCanvas(
    points: List<HistoryPoint>,
    accentColor: Color,
    unit: String,
    modifier: Modifier = Modifier
) {
    if (points.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sin datos en este periodo",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
        return
    }

    var selectedPointIndex by remember { mutableStateOf<Int?>(null) }
    val maxValue = (points.maxOfOrNull { it.value } ?: 1.0).coerceAtLeast(1.0)
    val dateFormatter = DateTimeFormatter.ofPattern("EEE d MMM", Locale.getDefault())

    Column(modifier = modifier.fillMaxWidth()) {
        // Indicador del punto seleccionado (al tocar una barra)
        val selectedPoint = selectedPointIndex?.let { points.getOrNull(it) } ?: points.lastOrNull()

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = selectedPoint?.date?.format(dateFormatter) ?: "Detalle",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = "${selectedPoint?.formattedValue ?: "0"} $unit",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .pointerInput(points) {
                    detectTapGestures { offset ->
                        val barWidthWithSpacing = size.width / points.size.toFloat()
                        val clickedIndex = (offset.x / barWidthWithSpacing).toInt()
                        if (clickedIndex in points.indices) {
                            selectedPointIndex = clickedIndex
                        }
                    }
                }
        ) {
            val totalBars = points.size
            val barSpacingRatio = if (totalBars <= 7) 0.35f else 0.2f
            val slotWidth = size.width / totalBars
            val barWidth = slotWidth * (1f - barSpacingRatio)
            val maxBarHeight = size.height - 30.dp.toPx()

            // Línea base
            drawLine(
                color = Color.LightGray.copy(alpha = 0.5f),
                start = Offset(0f, size.height - 20.dp.toPx()),
                end = Offset(size.width, size.height - 20.dp.toPx()),
                strokeWidth = 1.dp.toPx()
            )

            points.forEachIndexed { index, point ->
                val barHeight = ((point.value / maxValue) * maxBarHeight).toFloat().coerceAtLeast(4.dp.toPx())
                val left = index * slotWidth + (slotWidth - barWidth) / 2f
                val top = size.height - 20.dp.toPx() - barHeight

                val isSelected = index == (selectedPointIndex ?: (points.size - 1))
                val color = if (isSelected) accentColor else accentColor.copy(alpha = 0.4f)

                drawRoundRect(
                    color = color,
                    topLeft = Offset(left, top),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                )
            }
        }
    }
}
