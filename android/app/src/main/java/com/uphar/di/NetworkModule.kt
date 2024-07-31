package com.uphar.di
import com.jhalakXCorp.vent.bussinesss.domain.Utils.state.ErrorUtils
import com.uphar.bussinesss.domain.dataStore.authetication.LoginDatasource
import com.uphar.bussinesss.domain.dataStore.authetication.LoginDatasourceImpl
import com.uphar.bussinesss.domain.dataStore.common.CommonDatasource
import com.uphar.bussinesss.domain.dataStore.common.CommonDatasourceImpl
import com.uphar.datasource.retrofit.BOBRetrofitService
import com.uphar.datasource.retrofit.BOBRetrofitServiceImpl
import com.uphar.datasource.retrofit.BOBWebServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://smartbarodanode.azurewebsites.net")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideBOBWebServices(retrofit: Retrofit): BOBWebServices {
        return retrofit.create(BOBWebServices::class.java)
    }

    @Singleton
    @Provides
    fun provideBOBRetrofitService(
        bOBWebServices: BOBWebServices,
        errorUtils: ErrorUtils
    ): BOBRetrofitService {
        return BOBRetrofitServiceImpl(bOBWebServices, errorUtils)
    }

    @Singleton
    @Provides
    fun provideLoginDatasource(
        bOBRetrofitService: BOBRetrofitService
    ): LoginDatasource {
        return LoginDatasourceImpl(bOBRetrofitService)
    }

    @Singleton
    @Provides
    fun provideCommonDatasource(
        bOBRetrofitService: BOBRetrofitService
    ): CommonDatasource {
        return CommonDatasourceImpl(bOBRetrofitService)
    }

    @Singleton
    @Provides
    fun provideErrorUtils(retrofit: Retrofit): ErrorUtils {
        return ErrorUtils(retrofit)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppRetrofit
