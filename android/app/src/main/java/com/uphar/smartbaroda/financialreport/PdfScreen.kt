package com.uphar.smartbaroda.financialreport

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.uphar.bussinesss.domain.Utils.state.DataState
import com.uphar.datasource.networkmodel.Expenditure
import com.uphar.datasource.networkmodel.FinancialReportResponse
import com.uphar.smartbaroda.MainViewModel
import com.uphar.smartbaroda.R

@Composable
fun PdfViewerScreen() {
    val viewModel: MainViewModel = hiltViewModel()

    // State to manage the loading state
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.getFinancialReport("123456") // Replace with actual account number
    }

    var financialReportData by remember {
        mutableStateOf(FinancialReportResponse(
            savingsSuggestions = "",
            expenditure = Expenditure(
                anonymous = "",
                food = "",
                loan = "",
                groceries = "",
                travel = ""
            )
        ))
    }

    val dataStateFinancialReport by viewModel.financialReportResponseDataState

    LaunchedEffect(dataStateFinancialReport) {
        when (dataStateFinancialReport) {
            is DataState.Error -> {
                // Handle error state, display error message, etc.
                isLoading = false
            }
            is DataState.Loading -> {
                // Show loading indicator if necessary
                isLoading = true
            }
            is DataState.Success -> {
                val successState = dataStateFinancialReport as DataState.Success<FinancialReportResponse>
                Log.d("FinancialReport", successState.data.toString())
                financialReportData = successState.data
                isLoading = false
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val expenditure = financialReportData.expenditure
        val savingsSuggestions = financialReportData.savingsSuggestions

        ExpenditureSummaryScreen(
            expenditure = expenditure,
            savingsSuggestions = savingsSuggestions
        )
    }
}

//@Composable
//fun ExpenditureSummaryScreen(expenditure: Expenditure, savingsSuggestions: String) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//            .verticalScroll(rememberScrollState())
//    ) {
//        Text(
//            text = "Expenditure Summary",
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color(0xFF2196F3) // Change this to match your theme
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // List of expenditure items
//        val expenditureItems = listOf(
//            "Anonymous" to expenditure.anonymous,
//            "Food" to expenditure.food,
//            "Loan" to expenditure.loan,
//            "Groceries" to expenditure.groceries
//        )
//
//        expenditureItems.forEach { (label, value) ->
//            Card(
//                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
//                backgroundColor = Color(0xFFBBDEFB), // Change this to match your theme
//                shape = RoundedCornerShape(8.dp),
//                elevation = 4.dp
//            ) {
//                Text(
//                    text = "$label: $value",
//                    fontSize = 18.sp,
//                    color = Color(0xFF0D47A1), // Change this to match your theme
//                    modifier = Modifier.padding(16.dp)
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Special handling for Travel
//        Card(
//            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
//            backgroundColor = Color(0xFFBBDEFB), // Change this to match your theme
//            shape = RoundedCornerShape(8.dp),
//            elevation = 4.dp
//        ) {
//            Text(
//                text = "Travel",
//                fontSize = 18.sp,
//                color = Color(0xFF0D47A1), // Change this to match your theme
//                modifier = Modifier.padding(16.dp)
//            )
//        }
//
//        Card(
//            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
//            backgroundColor = Color(0xFFBBDEFB), // Change this to match your theme
//            shape = RoundedCornerShape(8.dp),
//            elevation = 4.dp
//        ) {
//            Text(
//                text = "${expenditure.travel}",
//                fontSize = 18.sp,
//                color = Color(0xFF0D47A1), // Change this to match your theme
//                modifier = Modifier.padding(16.dp)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Text(
//            text = "Savings Suggestions",
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color(0xFF2196F3) // Change this to match your theme
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            backgroundColor = Color(0xFFBBDEFB), // Change this to match your theme
//            shape = RoundedCornerShape(8.dp),
//            elevation = 4.dp
//        ) {
//            Text(
//                text = savingsSuggestions,
//                fontSize = 16.sp,
//                color = Color(0xFF0D47A1), // Change this to match your theme
//                modifier = Modifier.padding(16.dp)
//            )
//        }
//    }
//}


@Composable
fun ExpenditureSummaryScreen(expenditure: Expenditure, savingsSuggestions: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "Expenditure Summary",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2196F3) // Change this to match your theme
        )
        SummaryUI(expenditure)

//        Spacer(modifier = Modifier.height(16.dp))
//
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            backgroundColor = Color(0xFFBBDEFB), // Change this to match your theme
//            shape = RoundedCornerShape(8.dp),
//            elevation = 4.dp
//        ) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                Text(
//                    text = "Anonymous: ${expenditure.anonymous}",
//                    fontSize = 18.sp,
//                    color = Color(0xFF0D47A1) // Change this to match your theme
//                )
//                Text(
//                    text = "Food: ${expenditure.food}",
//                    fontSize = 18.sp,
//                    color = Color(0xFF0D47A1) // Change this to match your theme
//                )
//                Text(
//                    text = "Loan: ${expenditure.loan}",
//                    fontSize = 18.sp,
//                    color = Color(0xFF0D47A1) // Change this to match your theme
//                )
//                Text(
//                    text = "Groceries: ${expenditure.groceries}",
//                    fontSize = 18.sp,
//                    color = Color(0xFF0D47A1) // Change this to match your theme
//                )
//                Text(
//                    text = "Travel: ${expenditure.travel}",
//                    fontSize = 18.sp,
//                    color = Color(0xFF0D47A1) // Change this to match your theme
//                )
//            }
//        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Savings Suggestions",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2196F3) // Change this to match your theme
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color(0xFFBBDEFB), // Change this to match your theme
            shape = RoundedCornerShape(8.dp),
            elevation = 4.dp
        ) {
            Text(
                text = savingsSuggestions,
                fontSize = 16.sp,
                color = Color(0xFF0D47A1), // Change this to match your theme
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
//@Composable
//fun SummaryUI() {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//    ) {
//        Text(
//            text = "Summary",
//            fontSize = 20.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            SummaryCard(
//                iconResId = R.drawable.checked_image,  // Replace with your actual icon resource
//                label = "Active Projects",
//                value = "6",
//                valueColor = Color(0xFF4285F4),  // Blue color
//                backgroundColor = Color(0xFFE7F0FE)  // Light Blue background
//            )
//            Spacer(modifier = Modifier.width(16.dp))
//            SummaryCard(
//                iconResId = R.drawable.bob_logo,  // Replace with your actual icon resource
//                label = "Active Tasks",
//                value = "157",
//                valueColor = Color(0xFFEA4335),  // Red color
//                backgroundColor = Color(0xFFFDE7E6)  // Light Red background
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            SummaryCard(
//                iconResId = R.drawable.checked_image,  // Replace with your actual icon resource
//                label = "Project Completion",
//                value = "80%",
//                valueColor = Color(0xFF34A853),  // Green color
//                backgroundColor = Color(0xFFE6F4EA)  // Light Green background
//            )
//            Spacer(modifier = Modifier.width(16.dp))
//            SummaryCard(
//                iconResId = R.drawable.bob_logo,  // Replace with your actual icon resource
//                label = "Task Completion",
//                value = "96%",
//                valueColor = Color(0xFF34A853),  // Green color
//                backgroundColor = Color(0xFFE6F4EA)  // Light Green background
//            )
//        }
//    }
//}
//
//@Composable
//fun SummaryCard(
//    iconResId: Int,
//    label: String,
//    value: String,
//    valueColor: Color,
//    backgroundColor: Color
//) {
//    Card(
//        shape = RoundedCornerShape(8.dp),
//        elevation = 4.dp,
//        modifier = Modifier
//            .height(120.dp)
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,
//            modifier = Modifier
//                .fillMaxSize()
//                .background(backgroundColor)
//                .padding(16.dp)
//        ) {
//            Image(
//                painter = painterResource(id = iconResId),
//                contentDescription = null,
//                modifier = Modifier.size(40.dp)
//            )
//            Text(
//                text = label,
//                fontSize = 14.sp,
//                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
//            )
//            Text(
//                text = value,
//                fontSize = 18.sp,
//                fontWeight = FontWeight.Bold,
//                color = valueColor
//            )
//        }
//    }
//}

@Composable
fun SummaryUI( expenditure: Expenditure) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Summary",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            SummaryCard(
                iconResId = R.drawable.miscellaneous_icon,  // Replace with your actual icon resource
                label = "Anonymous",
                value = expenditure.anonymous,
                valueColor = Color(0xFF4285F4),  // Blue color
                backgroundColor = Color(0xFFE7F0FE)  // Light Blue background
            )
            Spacer(modifier = Modifier.width(8.dp))
            SummaryCard(
                iconResId = R.drawable.food_icon,  // Replace with your actual icon resource
                label = "Food",
                value = expenditure.food,
                valueColor = Color(0xFFEA4335),  // Red color
                backgroundColor = Color(0xFFFDE7E6)  // Light Red background
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            SummaryCard(
                iconResId = R.drawable.transaction_icon,  // Replace with your actual icon resource
                label = "Loan",
                value = expenditure.loan,
                valueColor = Color(0xFF34A853),  // Green color
                backgroundColor = Color(0xFFE6F4EA)  // Light Green background
            )
            Spacer(modifier = Modifier.width(8.dp))
            SummaryCard(
                iconResId = R.drawable.grcoier,  // Replace with your actual icon resource
                label = "Groceries",
                value = expenditure.groceries,
                valueColor = Color(0xFF34A853),  // Green color
                backgroundColor = Color(0xFFE6F4EA)  // Light Green background
            )
        }
    }
}

@Composable
fun SummaryCard(
    iconResId: Int,
    label: String,
    value: String,
    valueColor: Color,
    backgroundColor: Color
) {

        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = 4.dp,
            modifier = Modifier
                .height(120.dp)
                .width(150.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = label,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
                Text(
                    text = value,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = valueColor
                )
            }
        }
    }

