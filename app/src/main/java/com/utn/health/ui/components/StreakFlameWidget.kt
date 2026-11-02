package com.utn.health.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.utn.health.domain.model.StreakState
import com.utn.health.ui.theme.HealthFlameOrange
import com.utn.health.ui.theme.HealthFreezeCyan
import com.utn.health.ui.theme.HealthFreezeCyanDark
import com.utn.health.ui.theme.HealthHeartRateRed
import com.utn.health.ui.theme.HealthSuccessGreen
import com.utn.health.ui.theme.HealthWarningAmber
import java.util.Locale

@Composable
fun StreakFlameWidget(
    streak: StreakState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = streak.progressPercentage,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "streak_progress"
    )

    val flameColor = when {
        streak.isFrozenToday -> HealthFreezeCyan
        streak.isGoalCompletedToday -> HealthFlameOrange
        streak.isStreakAtRisk -> HealthWarningAmber
        streak.currentStreak > 0 -> HealthFlameOrange.copy(alpha = 0.8f)
        else -> Color.Gray
    }

    val flameBackground = when {
        streak.isFrozenToday -> HealthFreezeCyanDark.copy(alpha = 0.2f)
        streak.isGoalCompletedToday -> HealthFlameOrange.copy(alpha = 0.15f)
        streak.isStreakAtRisk -> HealthWarningAmber.copy(alpha = 0.15f)
        else -> Color.Gray.copy(alpha = 0.1f)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Indicador circular con la llama central
                Box(
                    modifier = Modifier.size(72.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { 1f },
                        modifier = Modifier.size(72.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        strokeWidth = 6.dp,
                    )
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.size(72.dp),
                        color = flameColor,
                        strokeWidth = 6.dp,
                        strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap
                    )

                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(flameBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        if (streak.isFrozenToday) {
                            Icon(
                                imageVector = Icons.Default.AcUnit,
                                contentDescription = "Racha Congelada",
                                tint = HealthFreezeCyan,
                                modifier = Modifier.size(28.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Racha de distancia",
                                tint = flameColor,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Información textual de la racha
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${streak.currentStreak}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (streak.currentStreak == 1) " día de racha" else " días de racha",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = when {
                            streak.isFrozenToday -> "❄️ Escudo activo (Racha protegida hoy)"
                            streak.isGoalCompletedToday -> "🔥 ¡Meta diaria cumplida! (+1 día)"
                            streak.isStreakRecoverable -> "⚡ Racha perdida. ¡Toca para repararla!"
                            else -> String.format(Locale.getDefault(), "Meta: %.1f km hoy (Faltan %.2f km)", streak.dailyDistanceGoalKm, streak.remainingDistanceKm)
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = when {
                            streak.isFrozenToday -> HealthFreezeCyan
                            streak.isGoalCompletedToday -> HealthSuccessGreen
                            streak.isStreakRecoverable -> HealthHeartRateRed
                            else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        },
                        fontWeight = if (streak.isGoalCompletedToday || streak.isFrozenToday) FontWeight.SemiBold else FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Contador de escudos / congeladores disponibles
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Escudos",
                            tint = HealthFreezeCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${streak.streakFreezesAvailable} escudos de congelamiento disponibles",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}
