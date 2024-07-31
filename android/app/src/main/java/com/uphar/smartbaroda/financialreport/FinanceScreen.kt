@file:OptIn(ExperimentalPermissionsApi::class)

package com.uphar.smartbaroda.financialreport

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.uphar.bussinesss.domain.Utils.state.DataState
import com.uphar.datasource.networkmodel.LoanRecommendationRequest
import com.uphar.smartbaroda.MainViewModel
import com.uphar.smartbaroda.NotificationService
import com.uphar.smartbaroda.R
import com.uphar.smartbaroda.aiPromptScreen.ChatMessageItem
import com.uphar.smartbaroda.aiPromptScreen.ChatMessageItemResponse
import com.uphar.smartbaroda.aiPromptScreen.DotsTyping
import com.uphar.smartbaroda.aiPromptScreen.MessageDataModel
import com.uphar.smartbaroda.aiPromptScreen.PolicyChatScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Random

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FinanceScreen(navController: NavController) {
    FinanceChatScreen(navController = navController)


}



//fun showBasicNotification(context: Context) {
//    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//    // Create a notification channel if running on Android O or above
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        val channelId = "water_reminder_channel"
//        val channelName = "Water Reminder Channel"
//        val channel = NotificationChannel(
//            channelId,
//            channelName,
//            NotificationManager.IMPORTANCE_HIGH
//        )
//        notificationManager.createNotificationChannel(channel)
//    }
//
//    val notification = NotificationCompat.Builder(context, "water_reminder_channel")
//        .setContentTitle("Water Reminder")
//        .setContentText("Time to drink a glass of water")
//        .setSmallIcon(R.drawable.fingure_tap)
//        .setPriority(NotificationCompat.PRIORITY_HIGH)
//        .setAutoCancel(true)
//        .build()
//
//    notificationManager.notify(
//        4,
//        notification
//    )
//}
//class WaterNotificationService(
//    private val context:Context
//)
//{
//    private val notificationManager=context.getSystemService(NotificationManager::class.java)
//    fun showBasicNotification(){
//        val notification=NotificationCompat.Builder(context,"water_notification")
//            .setContentTitle("Water Reminder")
//            .setContentText("Time to drink a glass of water")
//            .setSmallIcon(R.drawable.donut)
//            .setPriority(NotificationManager.IMPORTANCE_HIGH)
//            .setAutoCancel(true)
//            .build()
//
//        notificationManager.notify(
//            5,
//            notification
//        )
//    }
//
//    fun showExpandableNotification(){
//        val notification=NotificationCompat.Builder(context,"water_notification")
//            .setContentTitle("Water Reminder")
//            .setContentText("Time to drink a glass of water")
//            .setSmallIcon(R.drawable.icon)
//            .setPriority(NotificationManager.IMPORTANCE_HIGH)
//            .setAutoCancel(true)
//            .setStyle(
//                NotificationCompat
//                    .BigPictureStyle()
//                    .bigPicture(
//                        context.bitmapFromResource(
//                            R.drawable.app_installed
//                        )
//                    )
//            )
//            .build()
//        notificationManager.notify(8,notification)
//    }
//
//    private fun Context.bitmapFromResource(
//        @DrawableRes resId:Int
//    )= BitmapFactory.decodeResource(
//        resources,
//        resId
//    )
//}


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FinanceChatScreen(navController: NavController) {
    val viewModel: MainViewModel = hiltViewModel()
    LaunchedEffect(true) {
        viewModel.getAllPolicy()

    }
    val messages = remember { mutableStateListOf<MessageDataModel>() }
    val (message, setMessage) = remember { mutableStateOf("") }
    val showTypingAnimation = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
//    val dataStateReferAndReward by viewModel.policyDataState
//    var hasProcessed by remember { mutableStateOf(false) }
//    when (dataStateReferAndReward) {
//        is DataState.Error -> {
//            // Handle the error state
//        }
//
//        is DataState.Loading -> {
//            // Handle the loading state
//        }
//
//        is DataState.Success -> {
//            if (!hasProcessed) {
//                Log.d("referHistory", dataStateReferAndReward.toString())
//                val successData = (dataStateReferAndReward as DataState.Success).data
//                val currentTime = LocalDateTime.now()
//                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//                val formattedTime = currentTime.format(formatter)
//                if (successData is List<*>) {
//                    for (loan in successData) {
//                        messages.add(
//                            MessageDataModel(
//                                content = "Type: ${loan.type} \nDescription: ${loan.description} \nInterest: ${loan.premium.value}",
//                                sendBy = "system",
//                                createdAt = formattedTime,
//                                attachment = ""
//                            )
//                        )
//
//                    }
//                }
//                hasProcessed = true
//            }
//        }
//    }
    val dataStateReferAndRewardrecc by viewModel.financeChatBotNetworkResponseDataState
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
                messages.add(
                    MessageDataModel(
                        content = successData.answer,
                        sendBy = "system",
                        createdAt = "",
                        attachment = ""
                    )
                )
                showTypingAnimation.value=false
                hasProcessed2 = true
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Chat messages list
            FinanceChatMessages(
                messageData = messages,
                showTypingAnimation = showTypingAnimation.value,
                modifier = Modifier
                    .weight(1f) // Takes up the remaining space
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            // Spacer to push the input box to the bottom
            Spacer(modifier = Modifier.height(8.dp))
            // Chat input box
            FinanceChatInputBox(
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
//                            delay(3000)
//                            showTypingAnimation.value = false
//                            messages.add(MessageDataModel("bot", "Hi Uphar", "", LocalDateTime.now().toString()))
                            viewModel.getFinanceBotRecommendations(
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
fun FinanceChatMessages(
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
fun FinanceChatInputBox(
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