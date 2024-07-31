package com.uphar.smartbaroda.aiPromptScreen

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.uphar.bussinesss.domain.Utils.state.DataState
import com.uphar.datasource.networkmodel.LoanOfferResponse
import com.uphar.datasource.networkmodel.LoanRecommendationRequest
import com.uphar.smartbaroda.MainViewModel
import com.uphar.smartbaroda.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PolicyRecommendationScreen(navController: NavController) {
    PolicyChatScreen(navController = navController)
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PolicyChatScreen(navController: NavController) {
    val viewModel: MainViewModel = hiltViewModel()
    LaunchedEffect(true) {
        viewModel.getAllPolicy()

    }
    val messages = remember { mutableStateListOf<MessageDataModel>() }
    val (message, setMessage) = remember { mutableStateOf("") }
    val showTypingAnimation = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val dataStateReferAndReward by viewModel.policyDataState
    var hasProcessed by remember { mutableStateOf(false) }
    when (dataStateReferAndReward) {
        is DataState.Error -> {
            // Handle the error state
        }

        is DataState.Loading -> {
            // Handle the loading state
        }

        is DataState.Success -> {
            if (!hasProcessed) {
                Log.d("referHistory", dataStateReferAndReward.toString())
                val successData = (dataStateReferAndReward as DataState.Success).data
                val currentTime = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val formattedTime = currentTime.format(formatter)
                if (successData is List<*>) {
                    for (loan in successData) {
                        messages.add(
                            MessageDataModel(
                                content = "Type: ${loan.type} \nDescription: ${loan.description} \nInterest: ${loan.premium.value}",
                                sendBy = "system",
                                createdAt = formattedTime,
                                attachment = ""
                            )
                        )

                    }
                }
                hasProcessed = true
            }
        }
    }
    val dataStateReferAndRewardrecc by viewModel.policyRecommendationDataState
    var hasProcessed2 by remember { mutableStateOf(false) }
    when (dataStateReferAndRewardrecc) {
        is DataState.Error -> {
            // Handle the error state
        }

        is DataState.Loading -> {
            // Handle the loading state
        }

        is DataState.Success -> {
            if (!hasProcessed2) {
                Log.d("referHistory", dataStateReferAndRewardrecc.toString())
                val successData = (dataStateReferAndRewardrecc as DataState.Success).data
                val currentTime = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val formattedTime = currentTime.format(formatter)
                if (successData is List<*>) {
                    for (loan in successData) {

                        messages.add(
                            MessageDataModel(
                                content = "Type: ${loan.policyType} \nDescription:  ${loan.policyDescription} \nInterest: ${loan.premiumAmount}",
                                sendBy = "system",
                                createdAt = formattedTime,
                                attachment = ""
                            )
                        )
                    }
                }
                hasProcessed2 = true
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Chat messages list
            PolicyChatMessages(
                messageData = messages,
                showTypingAnimation = showTypingAnimation.value,
                modifier = Modifier
                    .weight(1f) // Takes up the remaining space
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            // Spacer to push the input box to the bottom
            Spacer(modifier = Modifier.height(8.dp))
            // Chat input box
            PolicyChatInputBox(
                message = message,
                onMessageChange = setMessage,
                onSendClick = {
                    if (message.isNotBlank()) {
                        messages.add(
                            MessageDataModel(
                                "user",
                                message,
                                "",
                                LocalDateTime.now().toString()
                            )
                        )
                        setMessage("")
                        coroutineScope.launch {
                            showTypingAnimation.value = true
                            delay(3000)
                            showTypingAnimation.value = false
//                            messages.add(MessageDataModel("bot", "Hi Uphar", "", LocalDateTime.now().toString()))
                            viewModel.getPolicyReccom(
                                loanRecommendationRequest = LoanRecommendationRequest(
                                    userPrompt = message
                                ),
                                accNo = "123456"
                            )
                            hasProcessed2 = false
                        }
                    }
                }
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PolicyChatMessages(
    messageData: List<MessageDataModel>,
    showTypingAnimation: Boolean,
    modifier: Modifier
) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        modifier = modifier
    ) {
        items(messageData) { message ->
            if (message.sendBy == "user") {
                ChatMessageItem(message)
            } else {
                ChatMessageItemResponse(message)
            }
        }
        if (showTypingAnimation) {
            item {
//                TypingIndicator()
                DotsTyping()
            }
        }
    }
}


@Composable
fun PolicyChatInputBox(
    message: String,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colors.surface, RoundedCornerShape(8.dp))
            .border(BorderStroke(1.dp, MaterialTheme.colors.onSurface))
            .padding(8.dp)
    ) {
        TextField(
            value = message,
            onValueChange = onMessageChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Type a message...") },
            singleLine = true
        )
        IconButton(onClick = onSendClick) {
            Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
        }
    }
}


