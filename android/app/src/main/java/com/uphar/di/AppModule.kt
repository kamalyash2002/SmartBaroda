package com.uphar.di

import android.content.Context
import com.uphar.bussinesss.domain.dataStore.basePreference.BasePreferencesManager
import com.uphar.bussinesss.domain.dataStore.basePreference.BasePreferencesManagerImpl
import com.uphar.smartbaroda.NotificationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDataStoreRepository(@ApplicationContext context: Context): BasePreferencesManager {
        return BasePreferencesManagerImpl(context)
    }

    @Provides
    fun provideNotificationService(@ApplicationContext context: Context): NotificationService {
        return NotificationService(context)
    }
}


/*@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NetworkConnection*/