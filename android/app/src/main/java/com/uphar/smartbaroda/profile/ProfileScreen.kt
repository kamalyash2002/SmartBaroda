package com.uphar.smartbaroda.profile

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uphar.bussinesss.domain.Utils.state.DataState
import com.uphar.bussinesss.domain.data.profile.ProfileDetailByAccountData
import com.uphar.bussinesss.domain.data.profile.ProfileResponseData
import com.uphar.bussinesss.domain.data.profile.SellerDetails
import com.uphar.bussinesss.domain.data.profile.User
import com.uphar.bussinesss.domain.dataStore.basePreference.BasePreferencesManagerImpl
import com.uphar.smartbaroda.MainViewModel
import com.uphar.smartbaroda.NavRoutes
import com.uphar.smartbaroda.R
import com.uphar.smartbaroda.financialreport.PayNowScreen
import com.uphar.smartbaroda.ui.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "profileContent") {
        composable("profileContent") { ProfileContent(navController) }

        composable("transactionHistoryContent") { TransactionHistoryContent(navController) }
        composable("payNowScreen") { PayNowScreen(navController) }

    }
}

@Composable
fun ProfileContent(navController: NavController) {
    val viewModel: MainViewModel = hiltViewModel()
    var accountDetail by remember {
        mutableStateOf(
            ProfileDetailByAccountData(
                user = User(
                    id = "anonymousId123",
                    accountNumber = 987654,
                    username = "anonymousUser",
                    panCard = "ANON1234",
                    cibilScore = 650,
                    type = "unknown",
                    gstin = "UNKNOWN1234",
                    password = "dummyPassword",
                    balance = 1000,
                    createdAt = "2024-07-30",
                    v = 1
                ),
                sellerDetails = SellerDetails(
                    id = "sellerId456",
                    gstNo = "GST123456",
                    sellerName = "Unnamed Seller",
                    type = "Not Defined",
                    desc = "No description available",
                    v = 1
                )
            )
        )
    }
    viewModel.getProfileHistory("123456")
    viewModel.getReferHistory()
    val dataStateReferAndReward by viewModel.profileDetailByAccountNoDataState
    when (dataStateReferAndReward) {
        is DataState.Error -> {

        }

        is DataState.Loading -> {

        }

        is DataState.Success -> {
            Log.d("referHistory",dataStateReferAndReward.toString())
            accountDetail= (dataStateReferAndReward as DataState.Success).data

        }
    }
    var noOfTransaction by remember { mutableIntStateOf(0) }
    val tdataStateReferAndReward by viewModel.profileDetailDataState
    LaunchedEffect(tdataStateReferAndReward) {
        when (tdataStateReferAndReward) {
            is DataState.Error -> {
                // Handle error state, display error message, etc.
            }

            is DataState.Loading -> {
                // Show loading indicator if necessary
            }

            is DataState.Success -> {
                val successState = tdataStateReferAndReward as DataState.Success<ProfileResponseData>
                Log.d("referHistory", successState.data.toString())

                noOfTransaction = successState.data.transactions.size
               }


        }
    }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Picture and Edit Icon
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        ) {
            Image(
                painter = painterResource(R.drawable.profile_svgrepo_com), // Replace with your image resource
                contentDescription = "Profile Picture",
                modifier = Modifier.fillMaxSize()
            )
            Icon(
                painter = painterResource(R.drawable.ic_edit), // Replace with your edit icon resource
                contentDescription = "Edit Profile",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
                    .size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Information
        Text(text = "Uphar Gaur", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(text = "Saving Account", color = Color.Gray, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "(+91)-7988204539", color = Color.Gray, fontSize = 14.sp)
        Text(text = "uphargaur@gmail.com", color = Color.Gray, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Wallet and Orders
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "₹${accountDetail.user.balance}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "Wallet", color = Color.Gray, fontSize = 14.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "${noOfTransaction}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "Transaction", color = Color.Gray, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Options List
        Column(modifier = Modifier.fillMaxWidth()) {
            OptionItem(
                icon = R.drawable.moneybag,
                text = "Pay Now",
                onClick = {
                    navController.navigate("payNowScreen")
                })
            OptionItem(
                icon = R.drawable.ic_favorites,
                text = "Your Favorites",
                onClick = { /* Handle click */ })
            OptionItem(
                icon = R.drawable.ic_payment,
                text = "Transactions",
                onClick = {
                    navController.navigate(NavRoutes.TRANSACTION_HISTORY_CONTENT)
                })
            OptionItem(
                icon = R.drawable.ic_tell_your_friend,
                text = "Tell Your Friend",
                onClick = { /* Handle click */ })
            OptionItem(
                icon = R.drawable.ic_promotions,
                text = "Promotions",
                onClick = { /* Handle click */ })
            OptionItem(
                icon = R.drawable.ic_settings,
                text = "Settings",
                onClick = { /* Handle click */ })
//            OptionItem(
//                icon = R.drawable.ic_financial_report,
//                text = "Get Financial Report",
//                onClick = {
//                    navController.navigate("pdfViewerScreen")
//                })
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Log Out Button
        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    val basePreferencesManager = BasePreferencesManagerImpl(context)
                    basePreferencesManager.logOut()
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                    (context as Activity).finish()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
        ) {
            Text(text = "Log Out", color = Color.White)
        }

    }
}

@Composable
fun OptionItem(icon: Int, text: String, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(15.dp),
        elevation = 4.dp,
        modifier = Modifier
            .wrapContentHeight()
            .padding(10.dp)
            .clickable { onClick() }
    ) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = text,
                modifier = Modifier.size(24.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = text, fontSize = 16.sp)
        }
    }
}