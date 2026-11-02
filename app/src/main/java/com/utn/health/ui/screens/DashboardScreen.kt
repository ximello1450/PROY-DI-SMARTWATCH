package com.utn.health.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.utn.health.ui.components.MetricCard
import com.utn.health.ui.components.StreakFlameWidget
import com.utn.health.ui.components.SyncStatusBadge
import com.utn.health.ui.theme.HealthCaloriesOrange
import com.utn.health.ui.theme.HealthDistanceTeal
import com.utn.health.ui.theme.HealthHeartRateRed
import com.utn.health.ui.theme.HealthSleepPurple
import com.utn.health.ui.theme.HealthStepsBlue
import com.utn.health.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToHeartRateDetail: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToStreak: () -> Unit,
    onNavigateToConnection: () -> Unit,
    onNavigateToPrivacy: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val summary = uiState.summary
    val streak = uiState.streak

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Health",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToStreak) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Rachas",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onNavigateToHistory) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Historial",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onNavigateToConnection) {
                        Icon(
                            imageVector = Icons.Default.Watch,
                            contentDescription = "Estado Conexión",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onNavigateToPrivacy) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Privacidad",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    SyncStatusBadge(
                        lastSync = summary.lastSync,
                        isFromCache = summary.isFromCache,
                        isRefreshing = uiState.isRefreshing,
                        onRefresh = { viewModel.refresh() }
                    )
                }

                item {
                    // Widget interactivo de Racha estilo Duolingo
                    StreakFlameWidget(
                        streak = streak,
                        onClick = onNavigateToStreak
                    )
                }

                item {
                    Text(
                        text = "Métricas de Hoy",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                // 1. Pasos
                item {
                    MetricCard(
                        title = "Pasos",
                        value = summary.stepsFormatted,
                        unit = "pasos",
                        icon = Icons.AutoMirrored.Filled.DirectionsWalk,
                        accentColor = HealthStepsBlue,
                        hasPermission = true,
                        hasData = summary.steps != null && summary.steps > 0
                    )
                }

                // 2. Distancia
                item {
                    MetricCard(
                        title = "Distancia",
                        value = summary.distanceFormatted,
                        icon = Icons.Default.FitnessCenter,
                        accentColor = HealthDistanceTeal,
                        hasPermission = true,
                        hasData = summary.distanceMeters != null && summary.distanceMeters > 0,
                        onClick = onNavigateToStreak
                    )
                }

                // 3. Calorías
                item {
                    MetricCard(
                        title = "Calorías quemadas",
                        value = summary.caloriesFormatted,
                        icon = Icons.Default.LocalFireDepartment,
                        accentColor = HealthCaloriesOrange,
                        hasPermission = true,
                        hasData = summary.caloriesKcal != null && summary.caloriesKcal > 0
                    )
                }

                // 4. Frecuencia cardíaca (al tocar abre detalle)
                item {
                    MetricCard(
                        title = "Frecuencia Cardíaca (Promedio)",
                        value = summary.heartRateAvgFormatted,
                        icon = Icons.Default.Favorite,
                        accentColor = HealthHeartRateRed,
                        hasPermission = true,
                        hasData = summary.heartRateAvg != null,
                        onClick = onNavigateToHeartRateDetail
                    )
                }

                // 5. Sueño (ventana nocturna de la noche anterior)
                item {
                    MetricCard(
                        title = "Sueño de anoche",
                        value = summary.sleepFormatted,
                        icon = Icons.Default.Nightlight,
                        accentColor = HealthSleepPurple,
                        hasPermission = true,
                        hasData = summary.sleepMinutes != null && summary.sleepMinutes > 0
                    )
                }

                item {
                    FilledTonalButton(
                        onClick = onNavigateToHistory,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.BarChart, contentDescription = null)
                        Text(
                            text = "  Ver Historial Completo (7 y 30 días)",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
