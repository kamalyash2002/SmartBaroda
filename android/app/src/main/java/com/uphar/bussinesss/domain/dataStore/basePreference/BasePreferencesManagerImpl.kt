package com.uphar.bussinesss.domain.dataStore.basePreference

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val TAG = "BasePrefManager::"

const val DataStore_NAME = "base_preferences"
val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = DataStore_NAME)

data class UserPreferences(
    val userId: String,
    val userName: String,
    val userEmail: String,
    val userMobile: String,
    val userProfilePic: String
)






class BasePreferencesManagerImpl constructor(
    private val context: Context
) : BasePreferencesManager {

    companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
//        val TEST_MODE = stringPreferencesKey("test_mode")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val REFRESH_TOKEN_EXPIRY = longPreferencesKey("refresh_token_expiry")

        val USER_ID = stringPreferencesKey("user_id")
        val USER_FIRST_NAME = stringPreferencesKey("user_first.name")
        val USER_LAST_NAME = stringPreferencesKey("user_last.name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_MOBILE = stringPreferencesKey("user_mobile")
        val USER_PROFILE_IMAGE = stringPreferencesKey("user_image")
        val USER_IS_NEW = booleanPreferencesKey("user_is.new")
        val USER_TYPE = stringPreferencesKey("user_type")
        val USER_ROLE = stringPreferencesKey("user_role")
        val USER_IS_EMAIL_VERIFIED = booleanPreferencesKey("user_is_email_verified")
        val USER_DEFAULT_BUSINESS_ID = stringPreferencesKey("user_default_business_id")
        val USER_AADHAAR_VERIFIED = booleanPreferencesKey("user_aadhaar_verified")
        val USER_IS_SLCM_USER = booleanPreferencesKey("user_is_slcm_user")

        val USER_LOGIN_STATUS = booleanPreferencesKey("user_login_status")
        val USER_INTRO_STATUS = booleanPreferencesKey("user_intro_view_status")

        val BUSINESS_ID = stringPreferencesKey("business_id")
        val BUSINESS_NAME = stringPreferencesKey("business_name")
        val BUSINESS_LOGO = stringPreferencesKey("business_logo")

        val ACTIVE_SUBSCRIPTION_ID = stringPreferencesKey("active_subscription_id")
        val BUSINESS_IS_ACTIVE = booleanPreferencesKey("business_is_active")
        val COMMODITY_ID = stringPreferencesKey("commodity_id")
        val COMMODITY_VAR = stringPreferencesKey("commodity_variant")
        val CMD_ID = stringPreferencesKey("cmd_id")
        val COMPANY = stringPreferencesKey("company")
        val UNIT_MES = stringPreferencesKey("unit_mes")
        val COMMODITY_IS_ACTIVE = booleanPreferencesKey("commodity_is_active")
        val SHOULD_SHOW_IMAGE_SHUFFLE_DIALOG = booleanPreferencesKey("shouldShowImageShuffleDialog")
        val SHOULD_SHOW_USE_HIGH_CONTRAST_BACKGROUND_DIALOG =
            booleanPreferencesKey("shouldShowUseHighContrastBackground")

        val BUSINESS_UNIT_ID = stringPreferencesKey("business_unit_id")
        val BUSINESS_UNIT_NAME = stringPreferencesKey("business_unit_name")
        val BUSINESS_UNIT_LATITUDE = stringPreferencesKey("business_unit_latitude")
        val BUSINESS_UNIT_LONGITUDE = stringPreferencesKey("business_unit_longitude")


        val ADDRESS_LABEL = stringPreferencesKey("address_label")
        val ADDRESS_DISTRICT=stringPreferencesKey("address_district")
        val ADDRESS_LINE1 = stringPreferencesKey("address_line1")
        val ADDRESS_AREA = stringPreferencesKey("address_area")
        val ADDRESS_CITY = stringPreferencesKey("address_city")
        val ADDRESS_STATE = stringPreferencesKey("address_state")
        val ADDRESS_PIN_CODE = intPreferencesKey("address_pin_code")
        val ADDRESS_LANDMARK = stringPreferencesKey("address_landmark")
        val ADDRESS_ID = stringPreferencesKey("address_id")

    }

    override suspend fun updateAccessToken(accessToken: String) {
        context.datastore.edit { preferences ->
            preferences[ACCESS_TOKEN] = "$accessToken"
            //preferences[USER_LOGIN_STATUS] = true
        }
    }
    override suspend fun updateAccountType(userType: String) {
        context.datastore.edit { preferences ->
            preferences[USER_TYPE] = userType
            //preferences[USER_LOGIN_STATUS] = true
        }
    }

    @SuppressLint("LongLogTag")
    override suspend fun getAccessToken() = context.datastore.data
        .catch { exeption ->
            if (exeption is IOException) {
                Log.e(TAG, "Error reading preferences: ", exeption)
                emit(emptyPreferences())
            } else {
                throw exeption
            }
        }
        .map { preferences ->
            preferences[ACCESS_TOKEN] ?: ""
        }


    @SuppressLint("LongLogTag")
    override suspend fun getTestMode() = context.datastore.data
        .catch { exeption ->
            if (exeption is IOException) {
                Log.e(TAG, "Error reading preferences: ", exeption)
                emit(emptyPreferences())
            } else {
                throw exeption
            }
        }
        .map { preferences ->
            preferences[USER_ROLE] ?: ""

        }

    @SuppressLint("LongLogTag")
    override suspend fun getAccountType()  = context.datastore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences: ", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[USER_TYPE] ?: ""

        }
    override suspend fun updateRefreshToken(refreshToken: String) {
        context.datastore.edit { preferences ->
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

    override suspend fun updateTestMode(refreshToken: String) {
        context.datastore.edit { preferences ->
            preferences[USER_ROLE] = refreshToken
        }
    }

    override suspend fun getRefreshToken() = context.datastore.data
        .catch { exeption ->
            if (exeption is IOException) {
                Log.e(TAG, "Error reading preferences: ", exeption)
                emit(emptyPreferences())
            } else {
                throw exeption
            }
        }
        .map { preferences ->
            preferences[REFRESH_TOKEN] ?: ""
        }


    override suspend fun updateRefreshTokenExpiry(refreshTokenExpireTime: Long) {
        context.datastore.edit { preferences ->
            preferences[REFRESH_TOKEN_EXPIRY] = refreshTokenExpireTime
        }
    }

    override suspend fun getRefreshTokenExpiry() = context.datastore.data
        .catch { exeption ->
            if (exeption is IOException) {
                Log.e(TAG, "Error reading preferences: ", exeption)
                emit(emptyPreferences())
            } else {
                throw exeption
            }
        }
        .map { preferences ->
            preferences[REFRESH_TOKEN_EXPIRY] ?: 0
        }

    override suspend fun logOut() {
        context.datastore.edit { preferences ->
            preferences[USER_ID] = ""
            preferences[USER_FIRST_NAME] = ""
            preferences[USER_LAST_NAME] = ""
            preferences[USER_EMAIL] = ""
            preferences[USER_MOBILE] = ""
            preferences[USER_PROFILE_IMAGE] = ""
            preferences[ACCESS_TOKEN] = ""
            preferences[USER_LOGIN_STATUS] = false
            preferences[USER_ROLE]= ""
            preferences[USER_TYPE]= ""
            preferences[ADDRESS_LABEL] = ""
            preferences[ADDRESS_LINE1] = ""
            preferences[ADDRESS_AREA] = ""
            preferences[ADDRESS_CITY] = ""
            preferences[ADDRESS_STATE] = ""
            preferences[ADDRESS_PIN_CODE] = 0
            preferences[ADDRESS_LANDMARK] = ""
            preferences[ADDRESS_ID] = ""
        }
    }

    override suspend fun updateUserId(userId: String) {
        context.datastore.edit { preferences ->
            preferences[USER_ID] = userId
            //preferences[USER_LOGIN_STATUS] = true
        }
    }

    override suspend fun getUserId(): Flow<String>  = context.datastore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences: ", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[USER_ID] ?: ""
        }

    override suspend fun updateUserDetails(
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
        isSLCMUser: Boolean,
    ) {
        context.datastore.edit { preferences ->
            preferences[USER_ID] = userId
            preferences[USER_FIRST_NAME] = userFirstName
            preferences[USER_LAST_NAME] = userLastName
            preferences[USER_EMAIL] = email
            preferences[USER_MOBILE] = mobile
            preferences[USER_PROFILE_IMAGE] = image
            preferences[USER_IS_NEW] = isNewUser
            preferences[USER_IS_EMAIL_VERIFIED] = isEmailVerified
            preferences[USER_DEFAULT_BUSINESS_ID] = defaultBusinessId
            preferences[USER_AADHAAR_VERIFIED] = isAadhaarVerified
            preferences[USER_IS_SLCM_USER] = isSLCMUser
        }
    }





}