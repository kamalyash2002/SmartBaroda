package com.uphar.bussinesss.domain.dataStore.common

import com.uphar.bussinesss.domain.Utils.state.DataState
import com.uphar.bussinesss.domain.data.login.RegisterCredentials
import com.uphar.bussinesss.domain.data.profile.AddTransaction
import com.uphar.bussinesss.domain.data.profile.ProfileDetailByAccountData
import com.uphar.bussinesss.domain.data.profile.ProfileResponseData
import com.uphar.datasource.networkmodel.AllPolicyItemResponse
import com.uphar.datasource.networkmodel.FinanceChatBotNetworkResponse
import com.uphar.datasource.networkmodel.FinancialReportResponse
import com.uphar.datasource.networkmodel.LoanOfferResponse
import com.uphar.datasource.networkmodel.LoanRecommendationRequest
import com.uphar.datasource.networkmodel.LoanRecommendationResponse
import com.uphar.datasource.networkmodel.PolicyRecommendationNetworkResponse
import com.uphar.datasource.networkmodel.SmartNotificationsNetworkResponse
import kotlinx.coroutines.flow.Flow


interface CommonDatasource {

    suspend fun gettoken(
        userid: String,
        password: String
    ): Flow<DataState<String>>

    suspend fun registerUser(
        registerRequest: RegisterCredentials
    ): Flow<DataState<String>>

    suspend fun getUserProfile(
        authToken: String
    ): Flow<DataState<ProfileResponseData>>

    suspend fun getUserProfileByAccountNO(
        authToken: String,
        accountNo :String
    ): Flow<DataState<ProfileDetailByAccountData>>

    suspend fun addTransaction(
        authToken: String,
        transaction : AddTransaction
    ): Flow<DataState<String>>


    suspend fun getloans(
    ):  Flow<DataState<List<LoanOfferResponse>>>


    suspend fun getPolicyDetails(
    ):  Flow<DataState<List<AllPolicyItemResponse>>>

    suspend fun getLoanRecommendations(
        accountId: String,
        request: LoanRecommendationRequest
    ): Flow<DataState<List<LoanRecommendationResponse>>>

    suspend fun getPolicyRecommendations(
        accountId: String,
        request: LoanRecommendationRequest
    ): Flow<DataState<List<PolicyRecommendationNetworkResponse>>>

    suspend fun getFinanceBotRecommendations(
        accountId: String,
        request: LoanRecommendationRequest
    ): Flow<DataState<FinanceChatBotNetworkResponse>>

    suspend fun getFinancialReport(
        accountId: String,
    ): Flow<DataState<FinancialReportResponse>>


    suspend fun getSmartNotifications(
        accountId: String,
    ): Flow<DataState<SmartNotificationsNetworkResponse>>
}