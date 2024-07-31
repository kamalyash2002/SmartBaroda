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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
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
import androidx.navigation.NavHostController
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
fun LoanRecommendationScreen(navController: NavController)
{
    ChatScreen(navController = navController)
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun ChatScreen(navController: NavController) {
    val viewModel: MainViewModel = hiltViewModel()
    LaunchedEffect (true){
        viewModel.getAllLoans()

    }
    val messages = remember { mutableStateListOf<MessageDataModel>() }
    val (message, setMessage) = remember { mutableStateOf("") }
    val showTypingAnimation = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val dataStateReferAndReward by viewModel.loanDataState
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
                        if (loan is LoanOfferResponse) {
                            messages.add(
                                MessageDataModel(
                                    content = "Type: ${loan.type} \nDescription: ${loan.desc} \nInterest: ${loan.interest.numberDecimal}",
                                    sendBy = "system",
                                    createdAt = formattedTime,
                                    attachment = ""
                                )
                            )
                        }
                    }
                }
                hasProcessed = true
            }
        }
    }
    val dataStateReferAndRewardrecc by viewModel.loanReccoDataState
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
                        showTypingAnimation.value = false
                            messages.add(
                                MessageDataModel(
                                    content = "Type: ${loan.loanType} \nDescription:  ${loan.loanAmount} \nInterest: ${loan.interestRate}",
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
            ChatMessages(
                messageData = messages,
                showTypingAnimation = showTypingAnimation.value,
                modifier = Modifier
                    .weight(1f) // Takes up the remaining space
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            // Spacer to push the input box to the bottom
            Spacer(modifier = Modifier.height(8.dp))
            // Chat input box
            ChatInputBox(
                message = message,
                onMessageChange = setMessage,
                onSendClick = {
                    if (message.isNotBlank()) {
                        messages.add(MessageDataModel("user", message, "", LocalDateTime.now().toString()))
                        setMessage("")
                        coroutineScope.launch {
                            showTypingAnimation.value = true
                            viewModel.setLoanReccoDataStateLoading()
//                            messages.add(MessageDataModel("bot", "Hi Uphar", "", LocalDateTime.now().toString()))
                            viewModel.getAllLoansReccom(
                                loanRecommendationRequest = LoanRecommendationRequest(
                                    userPrompt = message
                                ) ,
                                accNo = "123456"
                            )
                            hasProcessed2=false
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ChatTopBar(navController: NavController, userName: String) {
    androidx.compose.material.TopAppBar(
        title = { Text("Chat with $userName") },
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatMessages(messageData: List<MessageDataModel>, showTypingAnimation: Boolean, modifier: Modifier) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        modifier = modifier
    ) {
        items(messageData) { message ->
            if(message.sendBy=="user")
            {
                ChatMessageItem(message)
            }
            else
            {
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatMessageItem(message: MessageDataModel) {
    val isUser = message.sendBy == "user"
    Box(
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isUser) Color.Yellow else MaterialTheme.colors.surface)
                .padding(8.dp)
        ) {
            Text(text = message.content, style = MaterialTheme.typography.body1)
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatMessageItemResponse(message: MessageDataModel) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 8.dp,
            border = BorderStroke(2.dp, Color(0xFFFFA500)), // Orange color for the border
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = message.content, style = MaterialTheme.typography.body1)
                Spacer(modifier = Modifier.height(4.dp))
//                Text(text = message.createdAt, style = MaterialTheme.typography.caption)
            }
        }
    }
}

@Composable
fun DotsTyping() {
    val numberOfDots = 4
    val spaceBetween = 2.dp
    val maxOffset = (numberOfDots * 2).toFloat()
    val dotSize = 10.dp
    val dotColor: Color = colorResource(id = R.color.grey)
    val delayUnit = 200
    val duration = numberOfDots * delayUnit

    @Composable
    fun Dot(offset: Float) {
        Spacer(
            Modifier
                .size(dotSize)
                .offset(y = -offset.dp)
                .background(
                    color = dotColor,
                    shape = CircleShape
                )
        )
    }

    val infiniteTransition = rememberInfiniteTransition()

    @Composable
    fun animateOffsetWithDelay(delay: Int) = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(animation = keyframes {
            durationMillis = duration
            0f at delay with LinearEasing
            maxOffset at delay + delayUnit with LinearEasing
            0f at delay + (duration/2)
        }), label = ""
    )

    val offsets = arrayListOf<State<Float>>()
    for (i in 0 until numberOfDots) {
        offsets.add(animateOffsetWithDelay(delay = i * delayUnit))
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(top = maxOffset.dp)
    ) {
        offsets.forEach {
            Dot(it.value)
            Spacer(Modifier.width(spaceBetween))
        }
    }
}

@Composable
fun TypingIndicator() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text("Typing...", style = MaterialTheme.typography.body2)
    }
}

@Composable
fun ChatInputBox(
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

data class MessageDataModel(
    val sendBy: String,
    val content: String,
    val attachment: String,
    val createdAt: String?
)

