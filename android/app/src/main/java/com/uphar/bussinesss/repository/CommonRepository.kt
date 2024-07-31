package com.uphar.bussinesss.repository

import com.uphar.bussinesss.domain.Utils.exhaustive
import com.uphar.bussinesss.domain.Utils.state.DataState
import com.uphar.bussinesss.domain.data.login.RegisterCredentials
import com.uphar.bussinesss.domain.data.profile.AddTransaction
import com.uphar.bussinesss.domain.data.profile.ProfileDetailByAccountData
import com.uphar.bussinesss.domain.data.profile.ProfileResponseData
import com.uphar.bussinesss.domain.dataStore.basePreference.BasePreferencesManager
import com.uphar.bussinesss.domain.dataStore.common.CommonDatasource
import com.uphar.datasource.networkmodel.AllPolicyItemResponse
import com.uphar.datasource.networkmodel.FinanceChatBotNetworkResponse
import com.uphar.datasource.networkmodel.FinancialReportResponse
import com.uphar.datasource.networkmodel.LoanOfferResponse
import com.uphar.datasource.networkmodel.LoanRecommendationRequest
import com.uphar.datasource.networkmodel.LoanRecommendationResponse
import com.uphar.datasource.networkmodel.PolicyRecommendationNetworkResponse
import com.uphar.datasource.networkmodel.SmartNotificationsNetworkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody


class CommonRepository constructor(
    private val commonDatasource: CommonDatasource,
    val basePreferencesManager: BasePreferencesManager
) {

    suspend fun getToken(
        userid: String,
        password: String
    ): Flow<DataState<String>> = flow {

        commonDatasource.gettoken(userid = userid, password = password).collect { dataState ->
            when (dataState) {
                is DataState.Error -> {
                    emit(DataState.Error(dataState.exception))
                }

                is DataState.Loading -> {
                    emit(DataState.Loading(dataState.loading))
                }

                is DataState.Success -> {
                    emit(DataState.Success(dataState.data))
                }
            }.exhaustive
        }
    }

    suspend fun registerUser(
        registerRequest: RegisterCredentials
    ): Flow<DataState<String>> = flow {

        commonDatasource.registerUser(registerRequest).collect { dataState ->
            when (dataState) {
                is DataState.Error -> {
                    emit(DataState.Error(dataState.exception))
                }

                is DataState.Loading -> {
                    emit(DataState.Loading(dataState.loading))
                }

                is DataState.Success -> {
                    emit(DataState.Success(dataState.data))
                }
            }.exhaustive
        }
    }

    suspend fun getUserProfile(
        authToken: String
    ): Flow<DataState<ProfileResponseData>> = flow {
        commonDatasource.getUserProfile(authToken).collect { dataState ->
            when (dataState) {
                is DataState.Error -> {
                    emit(DataState.Error(dataState.exception))
                }

                is DataState.Loading -> {
                    emit(DataState.Loading(dataState.loading))
                }

                is DataState.Success -> {
                    emit(DataState.Success(dataState.data))
                }
            }.exhaustive
        }
    }

    suspend fun getUserProfileByAccountNO(
        authToken: String,
        accountNo: String
    ): Flow<DataState<ProfileDetailByAccountData>> = flow {
        commonDatasource.getUserProfileByAccountNO(authToken = authToken, accountNo = accountNo)
            .collect { dataState ->
                when (dataState) {
                    is DataState.Error -> {
                        emit(DataState.Error(dataState.exception))
                    }

                    is DataState.Loading -> {
                        emit(DataState.Loading(dataState.loading))
                    }

                    is DataState.Success -> {
                        emit(DataState.Success(dataState.data))
                    }
                }.exhaustive
            }
    }

    suspend fun addTransaction(
        authToken: String,
        transaction: AddTransaction
    ): Flow<DataState<String>> = flow {
        commonDatasource.addTransaction(authToken = authToken, transaction = transaction)
            .collect { dataState ->
                when (dataState) {
                    is DataState.Error -> {
                        emit(DataState.Error(dataState.exception))
                    }

                    is DataState.Loading -> {
                        emit(DataState.Loading(dataState.loading))
                    }

                    is DataState.Success -> {
                        emit(DataState.Success(dataState.data))
                    }
                }.exhaustive
            }
    }

    suspend fun getloans(
    ): Flow<DataState<List<LoanOfferResponse>>> = flow {
        commonDatasource.getloans().collect { dataState ->
            when (dataState) {
                is DataState.Error -> {
                    emit(DataState.Error(dataState.exception))
                }

                is DataState.Loading -> {
                    emit(DataState.Loading(dataState.loading))
                }

                is DataState.Success -> {
                    emit(DataState.Success(dataState.data))
                }
            }.exhaustive
        }
    }
    suspend fun getPolicyDetails(
    ):  Flow<DataState<List<AllPolicyItemResponse>>> = flow {
        commonDatasource.getPolicyDetails().collect { dataState ->
            when (dataState) {
                is DataState.Error -> {
                    emit(DataState.Error(dataState.exception))
                }

                is DataState.Loading -> {
                    emit(DataState.Loading(dataState.loading))
                }

                is DataState.Success -> {
                    emit(DataState.Success(dataState.data))
                }
            }.exhaustive
        }
    }


    suspend fun getLoanRecommendations(
        accountId: String,
        request: LoanRecommendationRequest
    ): Flow<DataState<List<LoanRecommendationResponse>>> = flow {
        commonDatasource.getLoanRecommendations(
            accountId = accountId,
            request = request
        ).collect { dataState ->
            when (dataState) {
                is DataState.Error -> {
                    emit(DataState.Error(dataState.exception))
                }

                is DataState.Loading -> {
                    emit(DataState.Loading(dataState.loading))
                }

                is DataState.Success -> {
                    emit(DataState.Success(dataState.data))
                }
            }.exhaustive
        }
    }

    suspend fun getPolicyRecommendations(
        accountId: String,
        request: LoanRecommendationRequest
    ): Flow<DataState<List<PolicyRecommendationNetworkResponse>>> = flow {
        commonDatasource.getPolicyRecommendations(
            accountId = accountId,
            request = request
        ).collect { dataState ->
            when (dataState) {
                is DataState.Error -> {
                    emit(DataState.Error(dataState.exception))
                }

                is DataState.Loading -> {
                    emit(DataState.Loading(dataState.loading))
                }

                is DataState.Success -> {
                    emit(DataState.Success(dataState.data))
                }
            }.exhaustive
        }
    }

    suspend fun getFinanceBotRecommendations(
        accountId: String,
        request: LoanRecommendationRequest
    ): Flow<DataState<FinanceChatBotNetworkResponse>> = flow {
        commonDatasource.getFinanceBotRecommendations(
            accountId = accountId,
            request = request
        ).collect { dataState ->
            when (dataState) {
                is DataState.Error -> {
                    emit(DataState.Error(dataState.exception))
                }

                is DataState.Loading -> {
                    emit(DataState.Loading(dataState.loading))
                }

                is DataState.Success -> {
                    emit(DataState.Success(dataState.data))
                }
            }.exhaustive
        }
    }

    suspend fun getFinancialReport(
        accountId: String,
    ): Flow<DataState<FinancialReportResponse>> = flow {
        commonDatasource.getFinancialReport(
            accountId = accountId,
        ).collect { dataState ->
            when (dataState) {
                is DataState.Error -> {
                    emit(DataState.Error(dataState.exception))
                }

                is DataState.Loading -> {
                    emit(DataState.Loading(dataState.loading))
                }

                is DataState.Success -> {
                    emit(DataState.Success(dataState.data))
                }
            }.exhaustive
        }
    }

    suspend fun getSmartNotifications(
        accountId: String,
    ): Flow<DataState<SmartNotificationsNetworkResponse>> = flow {
        commonDatasource.getSmartNotifications(
            accountId = accountId,
        ).collect { dataState ->
            when (dataState) {
                is DataState.Error -> {
                    emit(DataState.Error(dataState.exception))
                }

                is DataState.Loading -> {
                    emit(DataState.Loading(dataState.loading))
                }

                is DataState.Success -> {
                    emit(DataState.Success(dataState.data))
                }
            }.exhaustive
        }
    }

}