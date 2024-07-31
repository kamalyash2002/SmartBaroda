package com.uphar.smartbaroda.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.uphar.bussinesss.domain.Utils.DateTimeUtils
import com.uphar.bussinesss.domain.Utils.state.DataState
import com.uphar.bussinesss.domain.data.profile.ProfileResponseData
import com.uphar.bussinesss.domain.data.profile.Transaction
import com.uphar.smartbaroda.MainViewModel
import com.uphar.smartbaroda.R


@Composable
fun TransactionHistoryContent(navController: NavHostController) {
    val viewModel: MainViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        viewModel.getReferHistory()
    }

    val dataStateReferAndReward by viewModel.profileDetailDataState
    when (dataStateReferAndReward) {
        is DataState.Error -> {
            // Show an error message
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Error loading transaction history.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                // Optionally, you can show an error icon or image here
            }
        }

        is DataState.Loading -> {
            // Show a loading indicator
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Loading transaction history...",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                // Optionally, you can show a loading spinner or progress bar here
            }
        }

        is DataState.Success -> {
            Log.d("referHistory",dataStateReferAndReward.toString())
            val referHistory =
                (dataStateReferAndReward as DataState.Success<ProfileResponseData>).data.transactions
            Log.d("referHistory",referHistory.toString())
            if (referHistory.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ReferHistoryList(walletTransactions = referHistory)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.no_history_found),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.no_data),
                        contentDescription = stringResource(R.string.no_history_found),
                        modifier = Modifier.size(120.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ReferHistoryList(walletTransactions: List<Transaction>) {
    LazyColumn {
        items(walletTransactions.sortedByDescending { it.createdAt }) { walletTransaction ->
            ReferHistoryListItem(pointEntry = walletTransaction)
        }
    }
}

@Composable
fun ReferHistoryListItem(pointEntry: Transaction) {
    val transactionIcon = if (!pointEntry.isSender) {
        painterResource(id = R.drawable.ic_arrow_in)
    } else {
        painterResource(id = R.drawable.ic_arrow_out)
    }

    val transactionColor = if (!pointEntry.isSender) {
        colorResource(id = R.color.green_fun)
    } else {
        colorResource(id = R.color.red_dark)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = transactionIcon,
                    contentDescription = "Transaction Icon",
                    tint = transactionColor,
                    modifier = Modifier.size(50.dp)
                )
                Column {
                    Text(
                        text = pointEntry.description ?: "", // Use point entry's note if available
                        color = Color.Black,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = (if (pointEntry.isSender) pointEntry.receiverAccountNumber else pointEntry.senderAccountNumber).toString(),
                        color = Color.Black,
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )



                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "${pointEntry.amount}",
                            color = transactionColor,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Text(
                        text = "${
                            DateTimeUtils.getFormattedDate(
                                pointEntry.createdAt,
                                isTimeAgo = false
                            )
                        }",
                        color = colorResource(id = R.color.black).copy(0.5f)
                    )
                }
            }
        }
    }
}