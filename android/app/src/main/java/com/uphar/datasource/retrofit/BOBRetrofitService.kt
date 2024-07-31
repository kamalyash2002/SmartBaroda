package com.uphar.datasource.retrofit

import com.uphar.bussinesss.domain.Utils.state.DataState
import com.uphar.datasource.networkmodel.AllPolicyItemResponse
import com.uphar.datasource.networkmodel.FinanceChatBotNetworkResponse
import com.uphar.datasource.networkmodel.FinancialReportResponse
import com.uphar.datasource.networkmodel.LoanOfferResponse
import com.uphar.datasource.networkmodel.LoanRecommendationRequest
import com.uphar.datasource.networkmodel.LoanRecommendationResponse
import com.uphar.datasource.networkmodel.LoginRequest
import com.uphar.datasource.networkmodel.LoginResponse
import com.uphar.datasource.networkmodel.PolicyRecommendationNetworkResponse
import com.uphar.datasource.networkmodel.ProfileAccountResponse
import com.uphar.datasource.networkmodel.ProfileDetailByAccountResponse
import com.uphar.datasource.networkmodel.RegisterRequest
import com.uphar.datasource.networkmodel.SmartNotificationsNetworkResponse
import com.uphar.datasource.networkmodel.TransactionNetwork
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path


interface BOBRetrofitService {


    suspend fun getToken(
        loginRequest: LoginRequest
    ): Flow<DataState<LoginResponse>>


    suspend fun registerUser(
        registerRequest: RegisterRequest
    ): Flow<DataState<LoginResponse>>

    suspend fun getUserProfile(
        authToken: String
    ): Flow<DataState<ProfileAccountResponse>>


    suspend fun getloans(
    ):  Flow<DataState<List<LoanOfferResponse>>>


    suspend fun getPolicyDetails(
    ):  Flow<DataState<List<AllPolicyItemResponse>>>

    suspend fun getUserProfileByAccountNO(
        authToken: String,
        accountNo :String
    ): Flow<DataState<ProfileDetailByAccountResponse>>

    suspend fun addTransaction(
        authToken: String,
        transaction: TransactionNetwork
    ):  Flow<DataState<String>>


    suspend fun getLoanRecommendations(
        accountId: String,
      request: LoanRecommendationRequest
    ): Flow<DataState<List<LoanRecommendationResponse>>>

    suspend fun getPolicyRecommendations(
        accountId: String,
      request: LoanRecommendationRequest
    ): Flow<DataState<List<PolicyRecommendationNetworkResponse>>>

    suspend fun getFinanceRecommendations(
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