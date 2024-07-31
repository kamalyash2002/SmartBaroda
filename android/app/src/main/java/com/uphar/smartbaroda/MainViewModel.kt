package com.uphar.smartbaroda


import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.uphar.bussinesss.domain.Utils.exhaustive
import com.uphar.bussinesss.domain.Utils.state.DataState
import com.uphar.bussinesss.domain.data.login.RegisterCredentials
import com.uphar.bussinesss.domain.data.profile.AddTransaction
import com.uphar.bussinesss.domain.data.profile.ProfileDetailByAccountData
import com.uphar.bussinesss.domain.data.profile.ProfileResponseData
import com.uphar.bussinesss.domain.dataStore.basePreference.BasePreferencesManager
import com.uphar.bussinesss.repository.CommonRepository
import com.uphar.datasource.networkmodel.AllPolicyItemResponse
import com.uphar.datasource.networkmodel.FinanceChatBotNetworkResponse
import com.uphar.datasource.networkmodel.FinancialReportResponse
import com.uphar.datasource.networkmodel.LoanOfferResponse
import com.uphar.datasource.networkmodel.LoanRecommendationRequest
import com.uphar.datasource.networkmodel.LoanRecommendationResponse
import com.uphar.datasource.networkmodel.PolicyRecommendationNetworkResponse
import com.uphar.datasource.networkmodel.SmartNotificationsNetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val commonRepository: CommonRepository,
    val basePreferencesManager: BasePreferencesManager,
    private val notificationService: NotificationService,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val gson = Gson()
    private val _tokenQueryDataState =
        MutableStateFlow<DataState<String>>(DataState.Loading(false))
    val tokenQueryDataState: StateFlow<DataState<String>> get() = _tokenQueryDataState

    private val _registerTokenQueryDataState =
        MutableStateFlow<DataState<String>>(DataState.Loading(false))
    val registerTokenQueryDataState: StateFlow<DataState<String>> get() = _registerTokenQueryDataState

    private val _profileDetailDataState =
        mutableStateOf<DataState<ProfileResponseData>>(DataState.Loading(false))
    val profileDetailDataState: MutableState<DataState<ProfileResponseData>> get() = _profileDetailDataState

    private val _profileDetailByAccountNoDataState =
        mutableStateOf<DataState<ProfileDetailByAccountData>>(DataState.Loading(false))
    val profileDetailByAccountNoDataState: MutableState<DataState<ProfileDetailByAccountData>> get() = _profileDetailByAccountNoDataState


    private val _loanDataState =
        mutableStateOf<DataState<List<LoanOfferResponse>>>(DataState.Loading(false))
    val loanDataState: MutableState<DataState<List<LoanOfferResponse>>> get() = _loanDataState

   private val _policyDataState =
        mutableStateOf<DataState<List<AllPolicyItemResponse>>>(DataState.Loading(false))
    val policyDataState: MutableState<DataState<List<AllPolicyItemResponse>>> get() = _policyDataState

    private val _loanReccoDataState =
        mutableStateOf<DataState<List<LoanRecommendationResponse>>>(DataState.Loading(false))
    val loanReccoDataState: MutableState<DataState<List<LoanRecommendationResponse>>> get() = _loanReccoDataState
    fun setLoanReccoDataStateLoading() {
        _loanReccoDataState.value = DataState.Loading(true)
    }
    private val _financeChatBotNetworkResponseDataState =
        mutableStateOf<DataState<FinanceChatBotNetworkResponse>>(DataState.Loading(false))
    val financeChatBotNetworkResponseDataState: MutableState<DataState<FinanceChatBotNetworkResponse>> get() = _financeChatBotNetworkResponseDataState

    private val _policyRecommendationDataState =
        mutableStateOf<DataState<List<PolicyRecommendationNetworkResponse>>>(DataState.Loading(false))
    val policyRecommendationDataState: MutableState<DataState<List<PolicyRecommendationNetworkResponse>>> get() = _policyRecommendationDataState

    private val _financialReportResponseDataState =
        mutableStateOf<DataState<FinancialReportResponse>>(DataState.Loading(false))
    val financialReportResponseDataState: MutableState<DataState<FinancialReportResponse>> get() = _financialReportResponseDataState

   private val _smartNotificationsNetworkResponseResponseDataState =
        mutableStateOf<DataState<SmartNotificationsNetworkResponse>>(DataState.Loading(false))
    val smartNotificationsNetworkResponseResponseDataState: MutableState<DataState<SmartNotificationsNetworkResponse>> get() = _smartNotificationsNetworkResponseResponseDataState


    private val periodicScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    init {

        startSmartNotificationsPolling()
    }
    private fun startSmartNotificationsPolling() {
        periodicScope.launch {
            while (true) {
                val accountNo = basePreferencesManager.getUserId().first()
                getSmartNotifications(accountNo)
                delay(20000) // 20 seconds
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        periodicScope.cancel()
    }
    private fun setStateEvent(subscriptionEvent: UserSubscriptionEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (subscriptionEvent) {
                is UserSubscriptionEvent.GetToken -> {
                    commonRepository.getToken(
                        userid = subscriptionEvent.accNo,
                        password = subscriptionEvent.password
                    ).collect { token ->
                        when (token) {
                            is DataState.Error -> {
                                Log.e(
                                    "tokenRepository.gettoken::",
                                    "home viewModel : ${token.exception}"
                                )
                                _tokenQueryDataState.emit(token)
                            }

                            is DataState.Loading -> {
                                Log.e(
                                    "tokenRepository.gettoken::",
                                    "home viewModel : Is loading !!"
                                )
                                _tokenQueryDataState.emit(token)
                            }

                            is DataState.Success -> {
                                _tokenQueryDataState.emit(token)
                                basePreferencesManager.updateUserId(subscriptionEvent.accNo)
                                basePreferencesManager.updateAccessToken(token.data)
                                Log.e(
                                    "tokenRepository.gettoken::",
                                    "home viewModel : " + token.data
                                )


                            }
                        }.exhaustive


                    }
                }

                is UserSubscriptionEvent.Register -> {
                    commonRepository.registerUser(
                        registerRequest = subscriptionEvent.registerCredentials
                    ).collect { token ->
                        when (token) {
                            is DataState.Error -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : ${token.exception}"
                                )
                                _registerTokenQueryDataState.emit(token)
                            }

                            is DataState.Loading -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : Is loading !!"
                                )
                                _registerTokenQueryDataState.emit(token)
                            }

                            is DataState.Success -> {
                                basePreferencesManager.updateAccessToken(token.data)
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : " + token.data
                                )
                                _registerTokenQueryDataState.emit(token)

                            }
                        }.exhaustive


                    }
                }

                UserSubscriptionEvent.GetReferHistory -> {
                    commonRepository.getUserProfile(
                        authToken = basePreferencesManager.getAccessToken().first()
                    ).collect { token ->
                        when (token) {
                            is DataState.Error -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : ${token.exception}"
                                )
                                _profileDetailDataState.value = token.copy()
                            }

                            is DataState.Loading -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : Is loading !!"
                                )

                            }

                            is DataState.Success -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : " + token.data
                                )
                                _profileDetailDataState.value =
                                    token.copy(data = token.data)

                            }
                        }.exhaustive


                    }
                }

                is UserSubscriptionEvent.GetAccountDetail -> {
                    commonRepository.getUserProfileByAccountNO(
                        authToken = basePreferencesManager.getAccessToken().first(),
                        accountNo = subscriptionEvent.accountNo
                    ).collect { token ->
                        when (token) {
                            is DataState.Error -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : ${token.exception}"
                                )
                                _profileDetailByAccountNoDataState.value = token.copy()
                            }

                            is DataState.Loading -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : Is loading !!"
                                )

                            }

                            is DataState.Success -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : " + token.data
                                )
                                _profileDetailByAccountNoDataState.value =
                                    token.copy(data = token.data)

                            }
                        }.exhaustive


                    }
                }

                is UserSubscriptionEvent.AddTransactionDetail -> {
                    commonRepository.addTransaction(
                        authToken = basePreferencesManager.getAccessToken().first(),
                        transaction = subscriptionEvent.transaction
                    ).collect { token ->
                        when (token) {
                            is DataState.Error -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : ${token.exception}"
                                )
                            }

                            is DataState.Loading -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : Is loading !!"
                                )

                            }

                            is DataState.Success -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : " + token.data
                                )


                            }
                        }.exhaustive


                    }
                }

                UserSubscriptionEvent.GetAllLoans -> {
                    commonRepository.getloans(
                    ).collect { token ->
                        when (token) {
                            is DataState.Error -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : ${token.exception}"
                                )
                            }

                            is DataState.Loading -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : Is loading !!"
                                )

                            }

                            is DataState.Success -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : " + token.data
                                )

                                _loanDataState.value =
                                    token.copy(data = token.data)

                            }
                        }.exhaustive


                    }
                }

                is UserSubscriptionEvent.GetAccountDetailRecommedation -> {
                    commonRepository.getLoanRecommendations(
                        request = subscriptionEvent.loanRecommendationRequest,
                        accountId = subscriptionEvent.accountNo
                    ).collect { token ->
                        when (token) {
                            is DataState.Error -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : ${token.exception}"
                                )
                            }

                            is DataState.Loading -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : Is loading !!"
                                )

                            }

                            is DataState.Success -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : " + token.data
                                )

                                _loanReccoDataState.value =
                                    token.copy(data = token.data)

                            }
                        }.exhaustive


                    }
                }

                is UserSubscriptionEvent.GetFinancialReport -> {
                    commonRepository.getFinancialReport(
                        accountId = subscriptionEvent.accountNo
                    ).collect { token ->
                        when (token) {
                            is DataState.Error -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : ${token.exception}"
                                )
                            }

                            is DataState.Loading -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : Is loading !!"
                                )

                            }

                            is DataState.Success -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : " + token.data
                                )

                                _financialReportResponseDataState.value =
                                    token.copy(data = token.data)

                            }
                        }.exhaustive


                    }
                }

                UserSubscriptionEvent.GetAllPolicy ->{
                    commonRepository.getPolicyDetails(
                    ).collect { token ->
                        when (token) {
                            is DataState.Error -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : ${token.exception}"
                                )
                            }

                            is DataState.Loading -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : Is loading !!"
                                )

                            }

                            is DataState.Success -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : " + token.data
                                )

                                _policyDataState.value =
                                    token.copy(data = token.data)

                            }
                        }.exhaustive


                    }
                }

                is UserSubscriptionEvent.GetPolicyDetailRecommedation ->  {
                    commonRepository.getPolicyRecommendations(
                        request = subscriptionEvent.loanRecommendationRequest,
                        accountId = subscriptionEvent.accountNo
                    ).collect { token ->
                        when (token) {
                            is DataState.Error -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : ${token.exception}"
                                )
                            }

                            is DataState.Loading -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : Is loading !!"
                                )

                            }

                            is DataState.Success -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : " + token.data
                                )

                                _policyRecommendationDataState.value =
                                    token.copy(data = token.data)

                            }
                        }.exhaustive


                    }
                }

                is UserSubscriptionEvent.GetFinanceBotRecommendations -> {
                commonRepository.getFinanceBotRecommendations(
                    accountId = subscriptionEvent.accountNo,
                    request = subscriptionEvent.loanRecommendationRequest
                ).collect { token ->
                    when (token) {
                        is DataState.Error -> {
                            Log.e(
                                "register tokenRepository.gettoken::",
                                "home viewModel : ${token.exception}"
                            )
                        }

                        is DataState.Loading -> {
                            Log.e(
                                "register tokenRepository.gettoken::",
                                "home viewModel : Is loading !!"
                            )

                        }

                        is DataState.Success -> {
                            Log.e(
                                "register tokenRepository.gettoken::",
                                "home viewModel : " + token.data
                            )

                            _financeChatBotNetworkResponseDataState.value =
                                token.copy(data = token.data)

                        }
                    }.exhaustive


                }
            }

                is UserSubscriptionEvent.GetSmartNotifications -> {
                    commonRepository.getSmartNotifications(
                        accountId = subscriptionEvent.accountNo,
                    ).collect { token ->
                        when (token) {
                            is DataState.Error -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : ${token.exception}"
                                )
                            }

                            is DataState.Loading -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : Is loading !!"
                                )

                            }

                            is DataState.Success -> {
                                Log.e(
                                    "register tokenRepository.gettoken::",
                                    "home viewModel : " + token.data
                                )
                                notificationService.showExpandableNotification(token.data.answer)
                                _smartNotificationsNetworkResponseResponseDataState.value =
                                    token.copy(data = token.data)

                            }
                        }.exhaustive


                    }
                }
            }
        }
    }

    fun getToken(accNo: String, password: String) {
        setStateEvent(
            UserSubscriptionEvent.GetToken(
                accNo = accNo,
                password = password
            )
        )
    }

    fun registerUser(registerCredentials: RegisterCredentials) {
        setStateEvent(
            UserSubscriptionEvent.Register(
                registerCredentials
            )
        )
    }

    fun getReferHistory(
    ) {
        setStateEvent(
            UserSubscriptionEvent.GetReferHistory
        )
    }

    fun getProfileHistory(accNo: String) {
        setStateEvent(
            UserSubscriptionEvent.GetAccountDetail(accNo)
        )
    }

    fun addTransactionDetail(transaction: AddTransaction) {
        setStateEvent(
            UserSubscriptionEvent.AddTransactionDetail(transaction)
        )
    }

    fun getAllLoans() {
        setStateEvent(
            UserSubscriptionEvent.GetAllLoans
        )
    }
    fun getAllPolicy() {
        setStateEvent(
            UserSubscriptionEvent.GetAllPolicy
        )
    }
    fun getFinancialReport(accNo: String) {
        setStateEvent(
            UserSubscriptionEvent.GetFinancialReport(
                accountNo = accNo
            )
        )
    }
    fun getSmartNotifications(accNo: String) {
        setStateEvent(
            UserSubscriptionEvent.GetSmartNotifications(
                accountNo = accNo
            )
        )
    }


    fun getAllLoansReccom(
        accNo: String,
        loanRecommendationRequest: LoanRecommendationRequest
    ) {
        setStateEvent(
            UserSubscriptionEvent.GetAccountDetailRecommedation(
                accountNo = accNo,
                loanRecommendationRequest = loanRecommendationRequest
            )
        )
    }
    fun getPolicyReccom(
        accNo: String,
        loanRecommendationRequest: LoanRecommendationRequest
    ) {
        setStateEvent(
            UserSubscriptionEvent.GetPolicyDetailRecommedation(
                accountNo = accNo,
                loanRecommendationRequest = loanRecommendationRequest
            )
        )
    }
    fun getFinanceBotRecommendations(
        accNo: String,
        loanRecommendationRequest: LoanRecommendationRequest
    ) {
        setStateEvent(
            UserSubscriptionEvent.GetFinanceBotRecommendations(
                accountNo = accNo,
                loanRecommendationRequest = loanRecommendationRequest
            )
        )
    }


    sealed class UserSubscriptionEvent {
        data class GetToken(val accNo: String, val password: String) : UserSubscriptionEvent()
        data class Register(val registerCredentials: RegisterCredentials) : UserSubscriptionEvent()
        data object GetReferHistory : UserSubscriptionEvent()
        data object GetAllLoans : UserSubscriptionEvent()
        data object GetAllPolicy : UserSubscriptionEvent()
        data class AddTransactionDetail(val transaction: AddTransaction) : UserSubscriptionEvent()
        data class GetAccountDetail(val accountNo: String) : UserSubscriptionEvent()
        data class GetFinancialReport(val accountNo: String) : UserSubscriptionEvent()
        data class GetSmartNotifications(val accountNo: String) : UserSubscriptionEvent()
        data class GetAccountDetailRecommedation(
            val accountNo: String,
            val loanRecommendationRequest: LoanRecommendationRequest
        ) : UserSubscriptionEvent()
        data class GetFinanceBotRecommendations(
            val accountNo: String,
            val loanRecommendationRequest: LoanRecommendationRequest
        ) : UserSubscriptionEvent()
        data class GetPolicyDetailRecommedation(
            val accountNo: String,
            val loanRecommendationRequest: LoanRecommendationRequest
        ) : UserSubscriptionEvent()

    }


}