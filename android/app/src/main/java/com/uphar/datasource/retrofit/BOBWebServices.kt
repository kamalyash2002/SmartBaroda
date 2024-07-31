package com.uphar.datasource.retrofit

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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path


interface BOBWebServices {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>


    @POST("auth/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<LoginResponse>

    @GET("user/profile")
    suspend fun getUserProfile(
        @Header("x-auth-token") authToken: String
    ): Response<ProfileAccountResponse>

    @GET("loans")
    suspend fun getloans(
    ): Response<List<LoanOfferResponse>>

    @GET("user/profile/{accountNo}")
    suspend fun getUserProfileByAccountNO(
        @Header("x-auth-token") authToken: String,
        @Path("accountNo") accountNo: Int
    ): Response<ProfileDetailByAccountResponse>

    @POST("https://smartbarodanode.azurewebsites.net/transactions")
    suspend fun addTransaction(
        @Header("x-auth-token") authToken: String,
        @Body transaction: TransactionNetwork
    ): Response<TransactionNetwork>


    @POST("https://smartbarodagenai.azurewebsites.net/loanRecommender/{accountId}")
    suspend fun getLoanRecommendations(
        @Path("accountId") accountId: String,
        @Body request: LoanRecommendationRequest
    ): Response<List<LoanRecommendationResponse>>

    @POST("https://smartbarodagenai.azurewebsites.net/loanRecommender/{accountId}")
    suspend fun getPolicyRecommendations(
        @Path("accountId") accountId: String,
        @Body request: LoanRecommendationRequest
    ): Response<List<PolicyRecommendationNetworkResponse>>

    @POST("https://smartbarodagenai.azurewebsites.net/financialAdvisor/{accountId}")
    suspend fun getFinanceRecommendations(
        @Path("accountId") accountId: String,
        @Body request: LoanRecommendationRequest
    ): Response<FinanceChatBotNetworkResponse>

    @GET("https://smartbarodagenai.azurewebsites.net/reportGenerator/{accountId}")
    suspend fun getFinancialReport(
        @Path("accountId") accountId: String
    ): Response<FinancialReportResponse>

    @GET("https://smartbarodagenai.azurewebsites.net/smartPromotions/{accountId}")
    suspend fun getSmartNotifications(
        @Path("accountId") accountId: String
    ): Response<SmartNotificationsNetworkResponse>


    @GET("policy-details")
    suspend fun getPolicyDetails(
    ): Response<List<AllPolicyItemResponse>>

}