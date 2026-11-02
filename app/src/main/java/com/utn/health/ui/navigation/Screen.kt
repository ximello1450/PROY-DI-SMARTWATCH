package com.utn.health.ui.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object HeartRateDetail : Screen("heart_rate_detail")
    object History : Screen("history")
    object StreakDetail : Screen("streak_detail")
    object ConnectionStatus : Screen("connection_status")
    object Permissions : Screen("permissions")
    object PrivacyPolicy : Screen("privacy_policy")
}
