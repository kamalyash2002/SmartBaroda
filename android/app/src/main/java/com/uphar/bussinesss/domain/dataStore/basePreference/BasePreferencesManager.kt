package com.uphar.bussinesss.domain.dataStore.basePreference



import kotlinx.coroutines.flow.Flow

interface BasePreferencesManager {

    suspend fun updateAccessToken(accessToken: String)
    suspend fun getAccessToken(): Flow<String>
    suspend fun getTestMode(): Flow<String>

    suspend fun getAccountType(): Flow<String>
    suspend fun updateRefreshToken(accessToken: String)
    suspend fun updateTestMode(test: String)

    suspend fun updateAccountType(userType: String)
    suspend fun getRefreshToken(): Flow<String>

    suspend fun updateRefreshTokenExpiry(refreshTokeExpiry: Long)
    suspend fun getRefreshTokenExpiry(): Flow<Long>

    suspend fun logOut()
    suspend fun updateUserId(userId: String)
    suspend fun getUserId(): Flow<String>

    suspend fun updateUserDetails(
        userId: String,
        userFirstName: String,
        userLastName: String,
        email: String,
        mobile: String,
        image: String,
        isNewUser: Boolean,
        isEmailVerified: Boolean,
        defaultBusinessId: String,
        isAadhaarVerified: Boolean,
        isBusinessVerified: Boolean
    )














}