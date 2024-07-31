package com.uphar.datasource.retrofit

import android.util.Log
import com.uphar.bussinesss.domain.Utils.state.DataState
import com.jhalakXCorp.vent.bussinesss.domain.Utils.state.ErrorUtils
import com.uphar.bussinesss.domain.Utils.getErrorMessage
import com.uphar.bussinesss.domain.Utils.getErrorMessageWithOutNetworkError
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
import kotlinx.coroutines.flow.flow
import okhttp3.Response
import okhttp3.ResponseBody
import java.util.concurrent.CancellationException


class BOBRetrofitServiceImpl(
    private val bOBRetrofitService: BOBWebServices,
    private val errorUtils: ErrorUtils
) : BOBRetrofitService {
    override suspend fun getToken(loginRequest: LoginRequest): Flow<DataState<LoginResponse>> = flow {
        try {
            val response =
                bOBRetrofitService.login(request = loginRequest)
            if (response.isSuccessful) {
                Log.e("Token", response.body().toString())
                emit(DataState.Success(response.body()!!))
                emit(DataState.Loading(false))
            } else {
                val apiError = errorUtils.parseError(response)!!
                if (apiError.message.isNotEmpty()) {
                    emit(DataState.Error(CancellationException(apiError.message[0])))
                } else {
                    emit(DataState.Error(CancellationException("Something went wrong")))
                }
                emit(DataState.Loading(false))
            }
        } catch (e: Exception) {
            if (getErrorMessage(e).isNotEmpty())
                emit(DataState.Error(CancellationException(getErrorMessageWithOutNetworkError(e))))
            emit(DataState.Loading(false))
        }
    }

    override suspend fun registerUser(registerRequest: RegisterRequest): Flow<DataState<LoginResponse>> = flow {
        try {
            val response =
                bOBRetrofitService.registerUser( registerRequest= registerRequest)
            if (response.isSuccessful) {
                Log.e("Token", response.body().toString())
                emit(DataState.Success(response.body()!!))
                emit(DataState.Loading(false))
            } else {
                val apiError = errorUtils.parseError(response)!!
                if (apiError.message.isNotEmpty()) {
                    emit(DataState.Error(CancellationException(apiError.message[0])))
                } else {
                    emit(DataState.Error(CancellationException("Something went wrong")))
                }
                emit(DataState.Loading(false))
            }
        } catch (e: Exception) {
            if (getErrorMessage(e).isNotEmpty())
                emit(DataState.Error(CancellationException(getErrorMessageWithOutNetworkError(e))))
            emit(DataState.Loading(false))
        }
    }

    override suspend fun getUserProfile(authToken: String): Flow<DataState<ProfileAccountResponse>> = flow {
        try {
            val response =
                bOBRetrofitService.getUserProfile( authToken = authToken)
            if (response.isSuccessful) {
                Log.e("Token", response.body().toString())
                emit(DataState.Success(response.body()!!))
                emit(DataState.Loading(false))
            } else {
                val apiError = errorUtils.parseError(response)!!
                if (apiError.message.isNotEmpty()) {
                    emit(DataState.Error(CancellationException(apiError.message[0])))
                } else {
                    emit(DataState.Error(CancellationException("Something went wrong")))
                }
                emit(DataState.Loading(false))
            }
        } catch (e: Exception) {
            if (getErrorMessage(e).isNotEmpty())
                emit(DataState.Error(CancellationException(getErrorMessageWithOutNetworkError(e))))
            emit(DataState.Loading(false))
        }
    }

    override suspend fun getloans():  Flow<DataState<List<LoanOfferResponse>>> = flow {
        try {
            val response =
                bOBRetrofitService.getloans()
            if (response.isSuccessful) {
                Log.e("Token", response.body().toString())
                emit(DataState.Success(response.body()!!))
                emit(DataState.Loading(false))
            } else {
                val apiError = errorUtils.parseError(response)!!
                if (apiError.message.isNotEmpty()) {
                    emit(DataState.Error(CancellationException(apiError.message[0])))
                } else {
                    emit(DataState.Error(CancellationException("Something went wrong")))
                }
                emit(DataState.Loading(false))
            }
        } catch (e: Exception) {
            if (getErrorMessage(e).isNotEmpty())
                emit(DataState.Error(CancellationException(getErrorMessageWithOutNetworkError(e))))
            emit(DataState.Loading(false))
        }
    }

    override suspend fun getPolicyDetails():
            Flow<DataState<List<AllPolicyItemResponse>>> = flow {
        try {
            val response =
                bOBRetrofitService.getPolicyDetails()
            if (response.isSuccessful) {
                Log.e("Token", response.body().toString())
                emit(DataState.Success(response.body()!!))
                emit(DataState.Loading(false))
            } else {
                val apiError = errorUtils.parseError(response)!!
                if (apiError.message.isNotEmpty()) {
                    emit(DataState.Error(CancellationException(apiError.message[0])))
                } else {
                    emit(DataState.Error(CancellationException("Something went wrong")))
                }
                emit(DataState.Loading(false))
            }
        } catch (e: Exception) {
            if (getErrorMessage(e).isNotEmpty())
                emit(DataState.Error(CancellationException(getErrorMessageWithOutNetworkError(e))))
            emit(DataState.Loading(false))
        }
    }


    override suspend fun getUserProfileByAccountNO(
        authToken: String,
        accountNo: String
    ): Flow<DataState<ProfileDetailByAccountResponse>> = flow {
        try {
            val response =
                bOBRetrofitService.getUserProfileByAccountNO( authToken = authToken,accountNo =accountNo.toInt())
            if (response.isSuccessful) {
                Log.e("Token", response.body().toString())
                emit(DataState.Success(response.body()!!))
                emit(DataState.Loading(false))
            } else {
                val apiError = errorUtils.parseError(response)!!
                if (apiError.message.isNotEmpty()) {
                    emit(DataState.Error(CancellationException(apiError.message[0])))
                } else {
                    emit(DataState.Error(CancellationException("Something went wrong")))
                }
                emit(DataState.Loading(false))
            }
        } catch (e: Exception) {
            if (getErrorMessage(e).isNotEmpty())
                emit(DataState.Error(CancellationException(getErrorMessageWithOutNetworkError(e))))
            emit(DataState.Loading(false))
        }
    }

   override suspend fun addTransaction(
        authToken: String,
        transaction: TransactionNetwork
    ):  Flow<DataState<String>>  = flow {
       try {
           val response =
               bOBRetrofitService.addTransaction( authToken = authToken , transaction = transaction)
           if (response.isSuccessful) {
               Log.e("Token", response.body().toString())
               emit(DataState.Success(response.body()!!.status!!))
               emit(DataState.Loading(false))
           } else {
               val apiError = errorUtils.parseError(response)!!
               if (apiError.message.isNotEmpty()) {
                   emit(DataState.Error(CancellationException(apiError.message[0])))
               } else {
                   emit(DataState.Error(CancellationException("Something went wrong")))
               }
               emit(DataState.Loading(false))
           }
       } catch (e: Exception) {
           if (getErrorMessage(e).isNotEmpty())
               emit(DataState.Error(CancellationException(getErrorMessageWithOutNetworkError(e))))
           emit(DataState.Loading(false))
       }
   }

    override suspend fun getLoanRecommendations(
        accountId: String,
        request: LoanRecommendationRequest
    ): Flow<DataState<List<LoanRecommendationResponse>>> = flow {
        try {
            val response =
                bOBRetrofitService.getLoanRecommendations( accountId = accountId , request = request)
            if (response.isSuccessful) {
                Log.e("Token", response.body().toString())
                emit(DataState.Success(response.body()!!))
                emit(DataState.Loading(false))
            } else {
                val apiError = errorUtils.parseError(response)!!
                if (apiError.message.isNotEmpty()) {
                    emit(DataState.Error(CancellationException(apiError.message[0])))
                } else {
                    emit(DataState.Error(CancellationException("Something went wrong")))
                }
                emit(DataState.Loading(false))
            }
        } catch (e: Exception) {
            if (getErrorMessage(e).isNotEmpty())
                emit(DataState.Error(CancellationException(getErrorMessageWithOutNetworkError(e))))
            emit(DataState.Loading(false))
        }
    }

    override suspend fun getPolicyRecommendations(
        accountId: String,
        request: LoanRecommendationRequest
    ): Flow<DataState<List<PolicyRecommendationNetworkResponse>>> = flow {
        try {
            val response =
                bOBRetrofitService.getPolicyRecommendations( accountId = accountId , request = request)
            if (response.isSuccessful) {
                Log.e("Token", response.body().toString())
                emit(DataState.Success(response.body()!!))
                emit(DataState.Loading(false))
            } else {
                val apiError = errorUtils.parseError(response)!!
                if (apiError.message.isNotEmpty()) {
                    emit(DataState.Error(CancellationException(apiError.message[0])))
                } else {
                    emit(DataState.Error(CancellationException("Something went wrong")))
                }
                emit(DataState.Loading(false))
            }
        } catch (e: Exception) {
            if (getErrorMessage(e).isNotEmpty())
                emit(DataState.Error(CancellationException(getErrorMessageWithOutNetworkError(e))))
            emit(DataState.Loading(false))
        }
    }

    override suspend fun getFinanceRecommendations(
        accountId: String,
        request: LoanRecommendationRequest
    ): Flow<DataState<FinanceChatBotNetworkResponse>> = flow {
        try {
            val response =
                bOBRetrofitService.getFinanceRecommendations( accountId = accountId , request = request)
            if (response.isSuccessful) {
                Log.e("Token", response.body().toString())
                emit(DataState.Success(response.body()!!))
                emit(DataState.Loading(false))
            } else {
                val apiError = errorUtils.parseError(response)!!
                if (apiError.message.isNotEmpty()) {
                    emit(DataState.Error(CancellationException(apiError.message[0])))
                } else {
                    emit(DataState.Error(CancellationException("Something went wrong")))
                }
                emit(DataState.Loading(false))
            }
        } catch (e: Exception) {
            if (getErrorMessage(e).isNotEmpty())
                emit(DataState.Error(CancellationException(getErrorMessageWithOutNetworkError(e))))
            emit(DataState.Loading(false))
        }
    }
    override suspend fun getFinancialReport(accountId: String): Flow<DataState<FinancialReportResponse>> = flow {
        try {
            val response =
                bOBRetrofitService.getFinancialReport( accountId = accountId )
            if (response.isSuccessful) {
                Log.e("Token", response.body().toString())
                emit(DataState.Success(response.body()!!))
                emit(DataState.Loading(false))
            } else {
                val apiError = errorUtils.parseError(response)!!
                if (apiError.message.isNotEmpty()) {
                    emit(DataState.Error(CancellationException(apiError.message[0])))
                } else {
                    emit(DataState.Error(CancellationException("Something went wrong")))
                }
                emit(DataState.Loading(false))
            }
        } catch (e: Exception) {
            if (getErrorMessage(e).isNotEmpty())
                emit(DataState.Error(CancellationException(getErrorMessageWithOutNetworkError(e))))
            emit(DataState.Loading(false))
        }
    }

    override suspend fun getSmartNotifications(accountId: String): Flow<DataState<SmartNotificationsNetworkResponse>> = flow {
        try {
            val response =
                bOBRetrofitService.getSmartNotifications( accountId = accountId )
            if (response.isSuccessful) {
                Log.e("Token", response.body().toString())
                emit(DataState.Success(response.body()!!))
                emit(DataState.Loading(false))
            } else {
                val apiError = errorUtils.parseError(response)!!
                if (apiError.message.isNotEmpty()) {
                    emit(DataState.Error(CancellationException(apiError.message[0])))
                } else {
                    emit(DataState.Error(CancellationException("Something went wrong")))
                }
                emit(DataState.Loading(false))
            }
        } catch (e: Exception) {
            if (getErrorMessage(e).isNotEmpty())
                emit(DataState.Error(CancellationException(getErrorMessageWithOutNetworkError(e))))
            emit(DataState.Loading(false))
        }
    }

}