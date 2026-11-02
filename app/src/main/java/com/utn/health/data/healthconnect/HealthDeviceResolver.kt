package com.utn.health.data.healthconnect

import android.content.Context
import com.utn.health.domain.model.HealthDeviceSource

object HealthDeviceResolver {

    private val KNOWN_SOURCES = mapOf(
        "com.sec.android.app.shealth" to "Samsung Health",
        "com.google.android.apps.fitness" to "Google Fit",
        "com.fitbit.FitbitMobile" to "Fitbit",
        "com.garmin.android.apps.connectmobile" to "Garmin Connect",
        "com.huami.watch.hmwatchmanager" to "Zepp / Amazfit",
        "com.xiaomi.wearable" to "Mi Fitness",
        "com.huawei.health" to "Huawei Health",
        "com.polar.polarflow" to "Polar Flow",
        "com.whoop.android" to "WHOOP",
        "com.withings.wiscale2" to "Withings Health Mate"
    )

    fun resolveSource(packageName: String, context: Context? = null): HealthDeviceSource {
        val (displayName, deviceHint) = when {
            packageName.startsWith("com.android.healthconnect") || packageName.startsWith("com.google.android.apps.healthdata") -> {
                Pair("Health Connect", "Sensores y Dispositivos Sincronizados")
            }
            packageName.contains("shealth") -> {
                Pair("Samsung Health", "Samsung Smartwatch / Pulsera")
            }
            packageName.contains("fitbit") -> {
                Pair("Fitbit", "Pixel Watch / Fitbit")
            }
            packageName.contains("garmin") -> {
                Pair("Garmin Connect", "Smartwatch Garmin")
            }
            packageName.contains("huawei") -> {
                Pair("Huawei Health", "Smartwatch / Pulsera Huawei")
            }
            packageName.contains("honor") -> {
                Pair("Honor Health", "Smartwatch / Pulsera Honor")
            }
            packageName.contains("xiaomi") || packageName.contains("zepp") || packageName.contains("huami") -> {
                Pair("Mi Fitness / Zepp", "Smartwatch Xiaomi / Amazfit")
            }
            else -> {
                val known = KNOWN_SOURCES[packageName]
                val name = if (known != null) {
                    known
                } else if (context != null) {
                    try {
                        val pm = context.packageManager
                        val appInfo = pm.getApplicationInfo(packageName, 0)
                        pm.getApplicationLabel(appInfo).toString()
                    } catch (e: Exception) {
                        packageName
                    }
                } else {
                    packageName
                }
                Pair(name, "Dispositivo Inteligente")
            }
        }

        return HealthDeviceSource(
            packageName = packageName,
            appName = displayName,
            deviceModel = deviceHint,
            isPrimary = true
        )
    }
}
