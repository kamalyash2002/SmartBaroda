package com.uphar.di

import com.uphar.bussinesss.domain.dataStore.basePreference.BasePreferencesManager
import com.uphar.bussinesss.domain.dataStore.common.CommonDatasource
import com.uphar.bussinesss.repository.CommonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Singleton
    @Provides
    fun provideCommonRepository(
        commonDatasource: CommonDatasource, basePreferencesManager: BasePreferencesManager

    ): CommonRepository {
        return CommonRepository(commonDatasource, basePreferencesManager)
    }
}


