package com.utn.health.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Política de Privacidad", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Compromiso de Privacidad y Manejo de Datos de Salud",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Última actualización: Noviembre 2026\nAplicación: Health (UTN - ITIC-901)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        PolicySection(
                            title = "1. Datos recopilados",
                            content = "La aplicación 'Health' solicita acceso de solo lectura a los siguientes tipos de datos a través de Android Health Connect:\n• Pasos diarios (StepsRecord)\n• Distancia recorrida (DistanceRecord)\n• Calorías quemadas activas y basales (TotalCaloriesBurnedRecord)\n• Muestras de frecuencia cardíaca (HeartRateRecord)\n• Duración de sesiones de sueño (SleepSessionRecord)"
                        )

                        PolicySection(
                            title = "2. Finalidad del tratamiento",
                            content = "Los datos se utilizan exclusivamente para:\n• Presentar al usuario un resumen diario y estadísticas de salud en la interfaz.\n• Calcular el progreso de su meta y mantener su sistema de rachas de distancia.\n• Generar gráficos de tendencia histórica de 7 y 30 días."
                        )

                        PolicySection(
                            title = "3. Almacenamiento exclusivamente local",
                            content = "Todos los datos de salud se procesan y almacenan de forma estrictamente local en el dispositivo del usuario mediante una base de datos SQLite segura (Room). La aplicación NO cuenta con servidores en la nube, NO transmite datos a terceros, NO recopila información personal identificable ni monetiza datos con publicidad."
                        )

                        PolicySection(
                            title = "4. Solo lectura (Read-Only)",
                            content = "La aplicación únicamente consulta datos sincronizados por tu reloj (como el Samsung Galaxy Fit 3 o Pixel Watch). Nunca modifica, sobrescribe ni elimina información registrada en Health Connect o Samsung Health."
                        )

                        PolicySection(
                            title = "5. Control del usuario y revocación de permisos",
                            content = "El usuario tiene el control absoluto sobre sus permisos. Puede revocar el acceso a cualquiera de las métricas en cualquier momento desde los Ajustes del sistema de Android > Privacidad y Seguridad > Health Connect > Permisos de aplicaciones."
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

@Composable
private fun PolicySection(title: String, content: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
