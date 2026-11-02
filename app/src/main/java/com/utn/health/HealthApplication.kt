package com.utn.health

import android.app.Application
import com.utn.health.data.healthconnect.HealthConnectDataSource
import com.utn.health.data.local.HealthDatabase
import com.utn.health.data.repository.HealthRepository
import com.utn.health.data.repository.HealthRepositoryImpl
import com.utn.health.domain.usecase.CheckConnectionUseCase
import com.utn.health.domain.usecase.GetHeartRateDetailsUseCase
import com.utn.health.domain.usecase.GetHistoryUseCase
import com.utn.health.domain.usecase.GetTodaySummaryUseCase
import com.utn.health.domain.usecase.ManageStreakUseCase

class HealthApplication : Application() {

    lateinit var database: HealthDatabase
        private set

    lateinit var repository: HealthRepository
        private set

    lateinit var getTodaySummaryUseCase: GetTodaySummaryUseCase
        private set

    lateinit var getHistoryUseCase: GetHistoryUseCase
        private set

    lateinit var getHeartRateDetailsUseCase: GetHeartRateDetailsUseCase
        private set

    lateinit var checkConnectionUseCase: CheckConnectionUseCase
        private set

    lateinit var manageStreakUseCase: ManageStreakUseCase
        private set

    override fun onCreate() {
        super.onCreate()

        database = HealthDatabase.getInstance(this)
        val dataSource = HealthConnectDataSource(this)
        repository = HealthRepositoryImpl(
            healthProvider = dataSource,
            database = database
        )

        getTodaySummaryUseCase = GetTodaySummaryUseCase(repository)
        getHistoryUseCase = GetHistoryUseCase(repository)
        getHeartRateDetailsUseCase = GetHeartRateDetailsUseCase(repository)
        checkConnectionUseCase = CheckConnectionUseCase(repository)
        manageStreakUseCase = ManageStreakUseCase(repository)
    }
}
