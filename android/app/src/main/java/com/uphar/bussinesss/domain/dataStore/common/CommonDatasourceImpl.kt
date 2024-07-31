package com.uphar.bussinesss.domain.dataStore.common

import android.util.Log
import com.uphar.bussinesss.domain.Utils.exhaustive
import com.uphar.bussinesss.domain.Utils.state.DataState
import com.uphar.bussinesss.domain.data.login.RegisterCredentials
import com.uphar.bussinesss.domain.data.profile.AddTransaction
import com.uphar.bussinesss.domain.data.profile.ProfileDetailByAccountData
import com.uphar.bussinesss.domain.data.profile.ProfileResponseData
import com.uphar.bussinesss.domain.data.profile.SellerDetails
import com.uphar.bussinesss.domain.data.profile.User
import com.uphar.datasource.networkmodel.AllPolicyItemResponse
import com.uphar.datasource.networkmodel.FinanceChatBotNetworkResponse
import com.uphar.datasource.networkmodel.FinancialReportResponse
import com.uphar.datasource.networkmodel.LoanOfferResponse
import com.uphar.datasource.networkmodel.LoanRecommendationRequest
import com.uphar.datasource.networkmodel.LoanRecommendationResponse
import com.uphar.datasource.networkmodel.LoginRequest
import com.uphar.datasource.networkmodel.PolicyRecommendationNetworkResponse
import com.uphar.datasource.networkmodel.ProfileAccountResponse
import com.uphar.datasource.networkmodel.ProfileDetailByAccountResponse
import com.uphar.datasource.networkmodel.RegisterRequest
import com.uphar.datasource.networkmodel.SellerDetailsResponse
import com.uphar.datasource.networkmodel.SmartNotificationsNetworkResponse
import com.uphar.datasource.networkmodel.TransactionNetwork
import com.uphar.datasource.networkmodel.UserResponse
import com.uphar.datasource.retrofit.BOBRetrofitService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody


class CommonDatasourceImpl constructor(
    private val BOBRetrofitService: BOBRetrofitService
) : CommonDatasource {

    override suspend fun gettoken(userid: String, password: String): Flow<DataState<String>> =
        flow {
            BOBRetrofitService.getToken(
                loginRequest = LoginRequest(accNo = userid.toInt(), password = password)
            ).collect {
                when (it) {
                    is DataState.Error -> {
                        Log.d("gettoken::", "Api Error")
                        emit(DataState.Error(it.exception))
                    }

                    is DataState.Loading -> {
                        Log.d("gettoken::", "Api Loading")
                        emit(DataState.Loading(it.loading))
                    }

                    is DataState.Success -> {
                        Log.d("gettoken::", "Api Successful")
                        val token = it.data.token ?: ""
                        emit(DataState.Success(token))
                    }

                }.exhaustive
            }
        }

    override suspend fun registerUser(registerRequest: RegisterCredentials): Flow<DataState<String>> =
        flow {
            BOBRetrofitService.registerUser(
                registerRequest = RegisterRequest(
                    accNo = registerRequest.accNo.toInt(), password = registerRequest.password,
                    cibilScore = registerRequest.cibilScore.toInt(),
                    type = registerRequest.type,
                    username = registerRequest.username,
                    panCard = registerRequest.panCard,
                )
            ).collect {
                when (it) {
                    is DataState.Error -> {
                        Log.d("gettoken::", "Api Error")
                        emit(DataState.Error(it.exception))
                    }

                    is DataState.Loading -> {
                        Log.d("gettoken::", "Api Loading")
                        emit(DataState.Loading(it.loading))
                    }

                    is DataState.Success -> {
                        Log.d("gettoken::", "Api Successful")
                        val token = it.data.token ?: ""
                        emit(DataState.Success(token))
                    }

                }.exhaustive
            }
        }

    override suspend fun getUserProfile(authToken: String): Flow<DataState<ProfileResponseData>> =
        flow {
            BOBRetrofitService.getUserProfile(
                authToken = authToken
            ).collect {
                when (it) {
                    is DataState.Error -> {
                        Log.d("gettoken::", "Api Error")
                        emit(DataState.Error(it.exception))
                    }

                    is DataState.Loading -> {
                        Log.d("gettoken::", "Api Loading")
                        emit(DataState.Loading(it.loading))
                    }

                    is DataState.Success -> {
                        Log.d("gettoken::", "Api Successful")
                        val profileResponseData = it.data.toDomainModel()
                        emit(DataState.Success(profileResponseData))
                    }

                }.exhaustive
            }
        }

    override suspend fun getUserProfileByAccountNO(
        authToken: String,
        accountNo: String
    ): Flow<DataState<ProfileDetailByAccountData>> =
        flow {
            BOBRetrofitService.getUserProfileByAccountNO(
                authToken = authToken,
                accountNo = accountNo
            ).collect {
                when (it) {
                    is DataState.Error -> {
                        Log.d("gettoken::", "Api Error")
                        emit(DataState.Error(it.exception))
                    }

                    is DataState.Loading -> {
                        Log.d("gettoken::", "Api Loading")
                        emit(DataState.Loading(it.loading))
                    }

                    is DataState.Success -> {
                        Log.d("gettoken::", "Api Successful")
                        val profileResponseData = it.data.toDomain()
                        emit(DataState.Success(profileResponseData))
                    }

                }.exhaustive
            }
        }

    override suspend fun addTransaction(
        authToken: String,
        transaction: AddTransaction
    ): Flow<DataState<String>> =
        flow {
            BOBRetrofitService.addTransaction(
                authToken = authToken,
                transaction = TransactionNetwork(
                    amount = transaction.amount,
                    desc = transaction.desc,
                    type = transaction.type,
                    receiverAccNo = transaction.receiverAccNo,
                    status = transaction.status
                )
            ).collect {
                when (it) {
                    is DataState.Error -> {
                        Log.d("gettoken::", "Api Error")
                        emit(DataState.Error(it.exception))
                    }

                    is DataState.Loading -> {
                        Log.d("gettoken::", "Api Loading")
                        emit(DataState.Loading(it.loading))
                    }

                    is DataState.Success -> {
                        Log.d("gettoken::", "Api Successful")

                    }

                }.exhaustive
            }
        }

    override suspend fun getloans(): Flow<DataState<List<LoanOfferResponse>>> =
        flow {
            BOBRetrofitService.getloans(
            ).collect {
                when (it) {
                    is DataState.Error -> {
                        Log.d("gettoken::", "Api Error")
                        emit(DataState.Error(it.exception))
                    }

                    is DataState.Loading -> {
                        Log.d("gettoken::", "Api Loading")
                        emit(DataState.Loading(it.loading))
                    }

                    is DataState.Success -> {
                        Log.d("gettoken::", "Api Successful")
                        emit(DataState.Success(it.data))
                    }

                }.exhaustive
            }
        }

    override suspend fun getPolicyDetails(): Flow<DataState<List<AllPolicyItemResponse>>> =
        flow {
            BOBRetrofitService.getPolicyDetails(
            ).collect {
                when (it) {
                    is DataState.Error -> {
                        Log.d("gettoken::", "Api Error")
                        emit(DataState.Error(it.exception))
                    }

                    is DataState.Loading -> {
                        Log.d("gettoken::", "Api Loading")
                        emit(DataState.Loading(it.loading))
                    }

                    is DataState.Success -> {
                        Log.d("gettoken::", "Api Successful")
                        emit(DataState.Success(it.data))
                    }

                }.exhaustive
            }
        }


    override suspend fun getLoanRecommendations(
        accountId: String,
        request: LoanRecommendationRequest
    ): Flow<DataState<List<LoanRecommendationResponse>>> =
        flow {
            BOBRetrofitService.getLoanRecommendations(
                request = request,
                accountId = accountId
            ).collect {
                when (it) {
                    is DataState.Error -> {
                        Log.d("gettoken::", "Api Error")
                        emit(DataState.Error(it.exception))
                    }

                    is DataState.Loading -> {
                        Log.d("gettoken::", "Api Loading")
                        emit(DataState.Loading(it.loading))
                    }

                    is DataState.Success -> {
                        Log.d("gettoken::", "Api Successful")
                        emit(DataState.Success(it.data))
                    }

                }.exhaustive
            }
        }

    override suspend fun getPolicyRecommendations(
        accountId: String,
        request: LoanRecommendationRequest
    ): Flow<DataState<List<PolicyRecommendationNetworkResponse>>> =
        flow {
            BOBRetrofitService.getPolicyRecommendations(
                request = request,
                accountId = accountId
            ).collect {
                when (it) {
                    is DataState.Error -> {
                        Log.d("gettoken::", "Api Error")
                        emit(DataState.Error(it.exception))
                    }

                    is DataState.Loading -> {
                        Log.d("gettoken::", "Api Loading")
                        emit(DataState.Loading(it.loading))
                    }

                    is DataState.Success -> {
                        Log.d("gettoken::", "Api Successful")
                        emit(DataState.Success(it.data))
                    }

                }.exhaustive
            }
        }

    override suspend fun getFinanceBotRecommendations(
        accountId: String,
        request: LoanRecommendationRequest
    ): Flow<DataState<FinanceChatBotNetworkResponse>> =
        flow {
            BOBRetrofitService.getFinanceRecommendations(
                request = request,
                accountId = accountId
            ).collect {
                when (it) {
                    is DataState.Error -> {
                        Log.d("gettoken::", "Api Error")
                        emit(DataState.Error(it.exception))
                    }

                    is DataState.Loading -> {
                        Log.d("gettoken::", "Api Loading")
                        emit(DataState.Loading(it.loading))
                    }

                    is DataState.Success -> {
                        Log.d("gettoken::", "Api Successful")
                        emit(DataState.Success(it.data))
                    }

                }.exhaustive
            }
        }

    override suspend fun getFinancialReport(accountId: String): Flow<DataState<FinancialReportResponse>> =
        flow {
            BOBRetrofitService.getFinancialReport(
                accountId = accountId
            ).collect {
                when (it) {
                    is DataState.Error -> {
                        Log.d("gettoken::", "Api Error")
                        emit(DataState.Error(it.exception))
                    }

                    is DataState.Loading -> {
                        Log.d("gettoken::", "Api Loading")
                        emit(DataState.Loading(it.loading))
                    }

                    is DataState.Success -> {
                        Log.d("gettoken::", "Api Successful")
                        emit(DataState.Success(it.data))
                    }

                }.exhaustive
            }
        }

    override suspend fun getSmartNotifications(accountId: String): Flow<DataState<SmartNotificationsNetworkResponse>> =  flow {
        BOBRetrofitService.getSmartNotifications(
            accountId = accountId
        ).collect {
            when (it) {
                is DataState.Error -> {
                    Log.d("gettoken::", "Api Error")
                    emit(DataState.Error(it.exception))
                }

                is DataState.Loading -> {
                    Log.d("gettoken::", "Api Loading")
                    emit(DataState.Loading(it.loading))
                }

                is DataState.Success -> {
                    Log.d("gettoken::", "Api Successful")
                    emit(DataState.Success(it.data))
                }

            }.exhaustive
        }
    }
}

fun ProfileAccountResponse.toDomainModel(): ProfileResponseData {
    return ProfileResponseData(
        id = this.id,
        accountNumber = this.accountNumber,
        username = this.username,
        panCard = this.panCard,
        cibilScore = this.cibilScore,
        type = this.type,
        password = this.password,
        balance = this.balance,
        createdAt = this.createdAt,
        version = this.version,
        transactions = this.transactions.map { it.toDomainModel() },
        loans = this.loans.map { it.toDomainModel() }
    )
}

fun com.uphar.datasource.networkmodel.Transaction.toDomainModel(): com.uphar.bussinesss.domain.data.profile.Transaction {
    return com.uphar.bussinesss.domain.data.profile.Transaction(
        id = this.id,
        description = this.description,
        amount = this.amount,
        receiverAccountNumber = this.receiverAccountNumber,
        senderAccountNumber = this.senderAccountNumber,
        type = this.type,
        status = this.status,
        createdAt = this.createdAt,
        version = this.version,
        isSender = this.isSender
    )
}

fun com.uphar.datasource.networkmodel.Loan.toDomainModel(): com.uphar.bussinesss.domain.data.profile.Loan {
    return com.uphar.bussinesss.domain.data.profile.Loan(
        id = this.id,
        description = this.description,
        receiverAccountNumber = this.receiverAccountNumber,
        type = this.type,
        amount = this.amount,
        paid = this.paid,
        loanId = this.loanId,
        interest = this.interest,
        status = this.status,
        createdAt = this.createdAt,
        version = this.version
    )
}

fun ProfileDetailByAccountResponse.toDomain(): ProfileDetailByAccountData {
    return ProfileDetailByAccountData(
        user = user?.toDomain() ?: User(
            id = "",
            accountNumber = 0,
            username = "",
            panCard = "",
            cibilScore = 0,
            type = "",
            gstin = "",
            password = "",
            balance = 0,
            createdAt = "",
            v = 0
        ),
        sellerDetails = sellerDetails?.toDomain() ?: SellerDetails(
            id = "",
            gstNo = "",
            sellerName = "",
            type = "",
            desc = "",
            v = 0
        )
    )
}

fun UserResponse?.toDomain(): User {
    return User(
        id = this?.id ?: "",
        accountNumber = this?.accountNumber ?: 0,
        username = this?.username ?: "",
        panCard = this?.panCard ?: "",
        cibilScore = this?.cibilScore ?: 0,
        type = this?.type ?: "",
        gstin = this?.gstin ?: "",
        password = this?.password ?: "",
        balance = this?.balance ?: 0,
        createdAt = this?.createdAt ?: "",
        v = this?.v ?: 0
    )
}

fun SellerDetailsResponse?.toDomain(): SellerDetails {
    return SellerDetails(
        id = this?.id ?: "",
        gstNo = this?.gstNo ?: "",
        sellerName = this?.sellerName ?: "",
        type = this?.type ?: "",
        desc = this?.desc ?: "",
        v = this?.v ?: 0
    )
}
