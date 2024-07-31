package com.uphar.smartbaroda.financialreport

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.uphar.bussinesss.domain.Utils.state.DataState
import com.uphar.bussinesss.domain.data.profile.AddTransaction
import com.uphar.bussinesss.domain.data.profile.ProfileDetailByAccountData
import com.uphar.bussinesss.domain.data.profile.SellerDetails
import com.uphar.bussinesss.domain.data.profile.User
import com.uphar.smartbaroda.MainViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PayNowScreen(navController: NavHostController) {
    var amount by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var accountno by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
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
    var sellerName by remember { mutableStateOf("") }
    var checkdetail by remember { mutableStateOf(false) }
    var makeTransaction by remember { mutableStateOf(false) }
    val viewModel: MainViewModel = hiltViewModel()
    val balance = 1920.89
    val paymentMethods = listOf("My Wallet", "Credit Card", "Bank Account")
    var selectedPaymentMethod by remember { mutableStateOf(paymentMethods[0]) }
    var expanded by remember { mutableStateOf(false) }
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
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transfer Money") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Account no",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            TextField(
                value = accountno,
                onValueChange = {
                    if(accountno.length<7)
                    accountno = it
                    if(accountno.length==6)
                    {
                        checkdetail =true

                    }
                                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                leadingIcon = { Text(text = "", fontSize = 24.sp, fontWeight = FontWeight.Bold) }
            )
            if (checkdetail)
            {
                LaunchedEffect(Unit) {
                    viewModel.getProfileHistory(accountno)
                }
            }
            Text(
                text = "Seller Name ",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            TextField(
                value = accountDetail.sellerDetails.sellerName,
                onValueChange = {
//                    sellerName = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                leadingIcon = { Text(text = "", fontSize = 24.sp, fontWeight = FontWeight.Bold) }
            )
            Text(
                text = "Type",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            TextField(
                value = accountDetail.sellerDetails.type,
                onValueChange = {
//                    accountno = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                leadingIcon = { Text(text = "", fontSize = 24.sp, fontWeight = FontWeight.Bold) }
            )

            Text(
                text = "Amount",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            TextField(
                value = amount,
                onValueChange = { amount = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                leadingIcon = { Text(text = "₹", fontSize = 24.sp, fontWeight = FontWeight.Bold) }
            )


            Text(
                text = "Description",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            TextField(
                value = desc,
                onValueChange = { desc = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                leadingIcon = { Text(text = "", fontSize = 24.sp, fontWeight = FontWeight.Bold) }
            )
            Text(
                text = "Balance: ${accountDetail.user.balance} R\$",
                style = MaterialTheme.typography.body2,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf(100, 250, 500, 1000).forEach { preset ->
                    Button(
                        onClick = { amount = preset.toString() },
                        modifier = Modifier
                            .weight(1f)
                            .padding(2.dp)
                    ) {
                        Text("₹$preset ")
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
                    .padding(16.dp)
                    .clickable { expanded = true }
            ) {
                Text(
                    text = selectedPaymentMethod,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    paymentMethods.forEach { method ->
                        DropdownMenuItem(
                            onClick = {
                                selectedPaymentMethod = method
                                expanded = false
                            }
                        ) {
                            Text(text = method)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    makeTransaction =true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "Transfer Money")
            }

            Spacer(modifier = Modifier.height(16.dp))
            if(makeTransaction)
            {
                LaunchedEffect(true ) {
                    viewModel.addTransactionDetail(
                        AddTransaction(
                            type = accountDetail.sellerDetails.type,
                            desc = desc,
                            status = "Success",
                            amount = amount.toDouble(),
                            receiverAccNo = accountno.toLong()
                        )
                    )
                }
                navController.navigate("profileContent")
            }


        }
    }
}
