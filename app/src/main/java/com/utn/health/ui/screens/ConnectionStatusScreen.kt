package com.utn.health.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.utn.health.domain.model.HealthConnectSdkStatus
import com.utn.health.ui.theme.HealthFreezeCyan
import com.utn.health.ui.theme.HealthSuccessGreen
import com.utn.health.ui.theme.HealthWarningAmber
import com.utn.health.viewmodel.ConnectionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionStatusScreen(
    viewModel: ConnectionViewModel,
    onRequestPermissions: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val conn = uiState.connectionState
    var expandedBrand by remember { mutableStateOf<String?>("samsung") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estado de Conexión", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reintentar"
                        )
                    }
                }
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
                // Tarjeta de estado del SDK Health Connect
                item {
                    val (statusTitle, statusDesc, icon, color) = when (conn.sdkStatus) {
                        HealthConnectSdkStatus.Available -> Quadruple(
                            "Health Connect Disponible",
                            "La plataforma de salud de Android está integrada y lista para operar.",
                            Icons.Default.CheckCircle,
                            HealthSuccessGreen
                        )
                        HealthConnectSdkStatus.UpdateRequired -> Quadruple(
                            "Actualización Requerida",
                            "Health Connect requiere una actualización desde Google Play Store.",
                            Icons.Default.Warning,
                            HealthWarningAmber
                        )
                        HealthConnectSdkStatus.NotInstalled -> Quadruple(
                            "No Instalado",
                            "Health Connect no está instalado o habilitado en este dispositivo.",
                            Icons.Default.Error,
                            MaterialTheme.colorScheme.error
                        )
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(color.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = color,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = statusTitle,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = statusDesc,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Permisos Otorgados vs Faltantes
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "Permisos de Salud (${conn.grantedPermissions.size}/${conn.requiredPermissions.size})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            conn.requiredPermissions.forEach { perm ->
                                val isGranted = perm in conn.grantedPermissions
                                val friendlyName = when {
                                    perm.contains("STEPS") -> "Pasos"
                                    perm.contains("DISTANCE") -> "Distancia"
                                    perm.contains("TOTAL_CALORIES") -> "Calorías"
                                    perm.contains("HEART_RATE") -> "Frecuencia Cardíaca"
                                    perm.contains("SLEEP") -> "Sueño"
                                    else -> perm.substringAfterLast(".")
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = friendlyName, style = MaterialTheme.typography.bodyMedium)
                                    Icon(
                                        imageVector = if (isGranted) Icons.Default.CheckCircle else Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = if (isGranted) HealthSuccessGreen else HealthWarningAmber,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            if (conn.missingPermissions.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = onRequestPermissions,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Solicitar Permisos Faltantes")
                                }
                            }
                        }
                    }
                }

                // Fuentes y Smartwatches Detectados
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Watch,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Dispositivos y Fuentes de Salud",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            if (conn.detectedSources.isEmpty()) {
                                Text(
                                    text = "No se detectaron fuentes activas aún. Sincroniza la app compañera de tu smartwatch (Samsung Health, Fitbit, Garmin, etc.) con Health Connect.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                conn.detectedSources.forEach { source ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(HealthFreezeCyan.copy(alpha = 0.2f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Watch,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column {
                                            Text(
                                                text = source.appName,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Text(
                                                text = source.deviceModel ?: source.packageName,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Sección Generalizada de Configuración con Desplegables por Marca
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.PhoneAndroid,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Guía de Sincronización por Marca",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Health Connect es el bus central de Android. Despliega la marca de tu dispositivo para ver cómo habilitar la escritura de datos:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // 1. Samsung
                            BrandAccordionItem(
                                title = "Dispositivos Samsung (Galaxy Watch / Fit)",
                                isExpanded = expandedBrand == "samsung",
                                onToggle = { expandedBrand = if (expandedBrand == "samsung") null else "samsung" },
                                steps = listOf(
                                    "1. Abre Samsung Health en tu teléfono y toca los 3 puntos (o Perfil) > Ajustes.",
                                    "2. Selecciona 'Health Connect' y autoriza 'Permitir todo' o activa los permisos de escritura (Pasos, Distancia, Calorías, Frecuencia Cardíaca, Sueño).",
                                    "3. Frecuencia Cardíaca: Para que se registren muestras continuas, en Galaxy Wearable > Ajustes de Salud del reloj > Frecuencia cardíaca, selecciona 'Continuamente'.",
                                    "4. Desliza hacia abajo en Samsung Health para forzar la sincronización del reloj hacia el teléfono."
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // 2. Huawei / Honor
                            BrandAccordionItem(
                                title = "Dispositivos Huawei / Honor (Watch / Band)",
                                isExpanded = expandedBrand == "huawei",
                                onToggle = { expandedBrand = if (expandedBrand == "huawei") null else "huawei" },
                                steps = listOf(
                                    "1. Vincula tu reloj o pulsera en la app Huawei Health o Honor Health.",
                                    "2. Debido a los servicios HMS, instala la app puente oficial o 'Health Sync' desde Google Play Store.",
                                    "3. En Health Sync, selecciona como origen 'Huawei Health' y como destino 'Health Connect'.",
                                    "4. Concede los permisos de escritura requeridos para pasos, distancia y pulsaciones."
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // 3. Google / Wear OS / Fitbit
                            BrandAccordionItem(
                                title = "Dispositivos Google / Wear OS / Fitbit (Pixel Watch)",
                                isExpanded = expandedBrand == "google",
                                onToggle = { expandedBrand = if (expandedBrand == "google") null else "google" },
                                steps = listOf(
                                    "1. Abre la app Fitbit en tu teléfono.",
                                    "2. Ve a Ajustes de cuenta > Integraciones > Conectar con Health Connect.",
                                    "3. Concede los permisos de escritura para todas las métricas de actividad y ritmo cardíaco.",
                                    "4. Los relojes con Wear OS 4/5 sincronizan automáticamente en segundo plano."
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // 4. Garmin
                            BrandAccordionItem(
                                title = "Dispositivos Garmin (Forerunner / Venu / Fenix)",
                                isExpanded = expandedBrand == "garmin",
                                onToggle = { expandedBrand = if (expandedBrand == "garmin") null else "garmin" },
                                steps = listOf(
                                    "1. Abre Garmin Connect en tu teléfono.",
                                    "2. Toca el menú lateral > Ajustes > Aplicaciones conectadas.",
                                    "3. Toca 'Health Connect' y activa la sincronización de escritura para todas las categorías."
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // 5. Xiaomi / Amazfit
                            BrandAccordionItem(
                                title = "Dispositivos Xiaomi / Amazfit (Mi Fitness / Zepp)",
                                isExpanded = expandedBrand == "xiaomi",
                                onToggle = { expandedBrand = if (expandedBrand == "xiaomi") null else "xiaomi" },
                                steps = listOf(
                                    "1. Abre Mi Fitness o Zepp según la marca de tu dispositivo.",
                                    "2. Ve a Perfil > Aplicaciones y servicios de terceros > Health Connect.",
                                    "3. Autoriza la transferencia de pasos, distancia, calorías y ritmo cardíaco."
                                )
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun BrandAccordionItem(
    title: String,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    steps: List<String>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    steps.forEach { step ->
                        Text(
                            text = step,
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 3.dp)
                        )
                    }
                }
            }
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
