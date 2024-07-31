package com.uphar.smartbaroda.aiPromptScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.uphar.smartbaroda.NavRoutes
import com.uphar.smartbaroda.R
import com.uphar.smartbaroda.financialreport.FinanceScreen
import com.uphar.smartbaroda.financialreport.PdfViewerScreen
import com.uphar.smartbaroda.profile.ProfileContent
import org.json.JSONObject

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AIPromptScreen() {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavRoutes.AI_SCREEN) {
        composable(NavRoutes.LOAN_CONTENT) { LoanRecommendationScreen(navController) }
        composable(NavRoutes.POLICY_CONTENT) { PolicyRecommendationScreen(navController) }
        composable(NavRoutes.AI_SCREEN) { AiScreen(navController) }
        composable(NavRoutes.PDF_VIEWER_SCREEN) { PdfViewerScreen() }
        composable(NavRoutes.TRANSACTION_ANALYSIS_SCREEN) { FinanceScreen(navController) }
    }
    // Implement your AI Prompt Screen here

}


@Composable
fun AiScreen(navController: NavController)
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Your Personalized Bots", fontSize = 24.sp)

        LoanCard(
            title = "Get Loan Recommendation",
            subtitle = "Personalized for you",
            amount = "Up to ₹50,000",
            description = "Get the best loan options tailored to your needs.",
            percentage = "2.5% APR",
            amountColor = Color.Green,
            percentageColor = Color.Red,
            onButtonClick = {
               navController.navigate(NavRoutes.LOAN_CONTENT)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        PolicyCard(
            title = "Get Policy Now",
            subtitle = "Tailored Insurance Options",
            amount = "Coverage Up to ₹1,00,000",
            description = "Find the best insurance policies suited for your needs.",
            percentage = "5% Discount Available",
            amountColor = Color.Blue,
            percentageColor = Color.Magenta,
            onButtonClick = {
                navController.navigate(NavRoutes.POLICY_CONTENT)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        FinancialReportCard (
            reportTitle = "Annual Financial Overview",
            reportSubtitle = "Comprehensive Analysis",
            reportAmount = "Total Revenue: ₹5,00,00,000",
            reportDescription = "This report provides an in-depth analysis of the company's financial performance over the past year.",
            reportPercentage = "Growth: 10% YoY",
            amountColor = Color.Green,
            percentageColor = Color.Blue,
            onButtonClick = {
                navController.navigate(NavRoutes.PDF_VIEWER_SCREEN)
            }
        )
        FinancialBotReportCard(
            reportTitle = "Advisor Performance Review",
            reportSubtitle = "Recent Transaction Analysis",
            reportAmount = "Last Transaction Amount: ₹1,50,000",
            reportDescription = "This report analyzes the advisor's most recent transaction, highlighting key performance metrics and trends.",
            reportPercentage = "Growth: 15% QoQ",
            amountColor = Color.Green,
            percentageColor = Color.Blue,
            onButtonClick = {
                navController.navigate(NavRoutes.TRANSACTION_ANALYSIS_SCREEN)
            }
        )



    }
}

@Composable
fun LoanCard(
    title: String,
    subtitle: String,
    amount: String,
    description: String,
    percentage: String,
    amountColor: Color,
    percentageColor: Color,
    onButtonClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(15.dp),
        elevation = 4.dp,
        modifier = Modifier
            .wrapContentHeight()
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .wrapContentWidth(Alignment.End)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.robot_animation))
                LottieAnimation(
                    composition = lottieComposition,
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp),
                    iterations = 1
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onButtonClick,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow)
                ) {
                    Text(text = "Get Started", color = Color.Black)
                }
            }

        }
    }
}

@Composable
fun PolicyCard(
    title: String,
    subtitle: String,
    amount: String,
    description: String,
    percentage: String,
    amountColor: Color,
    percentageColor: Color,
    onButtonClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(15.dp),
        elevation = 4.dp,
        modifier = Modifier
            .wrapContentHeight()
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .wrapContentWidth(Alignment.End)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.policy_animation))
                LottieAnimation(
                    composition = lottieComposition,
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp),
                    iterations = 1
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onButtonClick,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan)
                ) {
                    Text(text = "Get Started", color = Color.Black)
                }
            }
        }
    }
}


@Composable
fun FinancialReportCard(
    reportTitle: String,
    reportSubtitle: String,
    reportAmount: String,
    reportDescription: String,
    reportPercentage: String,
    amountColor: Color,
    percentageColor: Color,
    onButtonClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(15.dp),
        elevation = 4.dp,
        modifier = Modifier
            .wrapContentHeight()
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = reportTitle,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Text(
                text = reportSubtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .wrapContentWidth(Alignment.End)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
//                Column(
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text(
//                        text = reportAmount,
//                        style = MaterialTheme.typography.headlineMedium.copy(color = amountColor),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 4.dp),
//                        textAlign = TextAlign.Start
//                    )
//                    Text(
//                        text = reportPercentage,
//                        style = MaterialTheme.typography.bodyMedium.copy(color = percentageColor),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 4.dp),
//                        textAlign = TextAlign.Start
//                    )
//                }
                val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.finanical_statrement))
                LottieAnimation(
                    composition = lottieComposition,
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp),
                    iterations = 1
                )
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = onButtonClick,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan)
                ) {
                    Text(text = "View Details", color = Color.Black)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = reportDescription,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        }
    }
}


@Composable
fun FinancialBotReportCard(
    reportTitle: String,
    reportSubtitle: String,
    reportAmount: String,
    reportDescription: String,
    reportPercentage: String,
    amountColor: Color,
    percentageColor: Color,
    onButtonClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(15.dp),
        elevation = 4.dp,
        modifier = Modifier
            .wrapContentHeight()
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = reportTitle,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Text(
                text = reportSubtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .wrapContentWidth(Alignment.End)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
//                Column(
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text(
//                        text = reportAmount,
//                        style = MaterialTheme.typography.headlineMedium.copy(color = amountColor),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 4.dp),
//                        textAlign = TextAlign.Start
//                    )
//                    Text(
//                        text = reportPercentage,
//                        style = MaterialTheme.typography.bodyMedium.copy(color = percentageColor),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 4.dp),
//                        textAlign = TextAlign.Start
//                    )
//                }
                val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_finanical))
                LottieAnimation(
                    composition = lottieComposition,
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp),
                    iterations = 1
                )
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = onButtonClick,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan)
                ) {
                    Text(text = "View Details", color = Color.Black)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = reportDescription,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        }
    }
}

