package com.utn.health.domain.model

import java.time.Instant

data class HealthDeviceSource(
    val packageName: String,
    val appName: String,
    val deviceModel: String? = null,
    val isPrimary: Boolean = false
)

sealed interface HealthConnectSdkStatus {
    object Available : HealthConnectSdkStatus
    object UpdateRequired : HealthConnectSdkStatus
    object NotInstalled : HealthConnectSdkStatus
}

data class ConnectionState(
    val sdkStatus: HealthConnectSdkStatus = HealthConnectSdkStatus.NotInstalled,
    val requiredPermissions: Set<String> = emptySet(),
    val grantedPermissions: Set<String> = emptySet(),
    val detectedSources: List<HealthDeviceSource> = emptyList(),
    val lastSyncTime: Instant? = null,
    val isReady: Boolean = false
) {
    val missingPermissions: Set<String>
        get() = requiredPermissions - grantedPermissions

    val hasAllPermissions: Boolean
        get() = requiredPermissions.isNotEmpty() && missingPermissions.isEmpty()
}
