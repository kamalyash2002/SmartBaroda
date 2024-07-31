package com.uphar.smartbaroda

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.uphar.smartbaroda.ui.theme.SmartBarodaTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.uphar.bussinesss.domain.Utils.state.DataState
import com.uphar.bussinesss.domain.data.profile.ProfileResponseData
import com.uphar.smartbaroda.aiPromptScreen.AIPromptScreen
import com.uphar.smartbaroda.login.LoginForm
import com.uphar.smartbaroda.profile.ProfileScreen
import com.uphar.smartbaroda.profile.ReferHistoryList
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(navController, startDestination = ScreenConstants.Dashboard.route, Modifier.padding(innerPadding)) {
            composable(ScreenConstants.Dashboard.route) { FinancialDashboard() }
            composable(ScreenConstants.AIPrompt.route) { AIPromptScreen() }
            composable(ScreenConstants.Profile.route) { ProfileScreen() }
        }
    }
}






@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(ScreenConstants.Dashboard, ScreenConstants.AIPrompt, ScreenConstants.Profile)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val lightOrange = Color(0xFFfa7b31) // Light Orange (you can adjust the hex value as needed)

    BottomNavigation(
        backgroundColor = lightOrange,
        contentColor = Color.White // This color will apply to the icons and text
    ) {
        items.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    androidx.compose.material3.Icon(
                        painter = painterResource(id = screen.iconResId),
                        contentDescription = screen.title
                    )
                },
                label = {
                    Text(
                        text = screen.title,
                        color = if (currentRoute == screen.route) Color.White else Color.Black
                    )
                },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route)
                    }
                },
                selectedContentColor = Color.White, // Color when item is selected
                unselectedContentColor = Color.Black // Color when item is not selected
            )
        }
    }
}



@Composable
fun FinancialDashboard() {
    val incomes = listOf(10000f, 8000f, 6000f, 7000f, 9000f, 10000f, 12000f, 8000f, 11000f, 9500f, 10500f, 11500f)
    val expenses = listOf(4000f, 3000f, 5000f, 60/00f, 5000f, 4000f, 4500f, 4700f, 5200f, 4800f, 4900f, 5000f)
    val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    var  currBalance by remember { mutableIntStateOf(0) }
    var expensesAmount by remember { mutableIntStateOf(0) }
    var incomeAmount by remember { mutableIntStateOf(0) }
    val viewModel: MainViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        viewModel.getReferHistory()
    }

    val dataStateReferAndReward by viewModel.profileDetailDataState
    LaunchedEffect(dataStateReferAndReward) {
        when (dataStateReferAndReward) {
            is DataState.Error -> {
                // Handle error state, display error message, etc.
            }

            is DataState.Loading -> {
                // Show loading indicator if necessary
            }

            is DataState.Success -> {
                val successState = dataStateReferAndReward as DataState.Success<ProfileResponseData>
                Log.d("referHistory", successState.data.toString())

                currBalance = successState.data.balance

                // Recalculate amounts only when data changes
                expensesAmount = successState.data.transactions.filter { it.isSender }.sumOf { it.amount }
                incomeAmount = successState.data.transactions.filter { !it.isSender }.sumOf { it.amount }

                Log.d("Amounts", "Total Expenses: $expensesAmount, Total Income: $incomeAmount")
            }


        }
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        FinancialCard(
            title = "Current balance",
            subtitle = "This month",
            amount = "₹${currBalance}",
            description = "This month your final balance has increased",
            percentage = "+5%",
            amountColor = Color(0xFF1A2FA4),
            percentageColor = Color(0xFF28A745)
        )
        Spacer(modifier = Modifier.height(10.dp))
        FinancialCard(
            title = "Expenses",
            subtitle = "This week",
            amount = "-₹${expensesAmount}",
            description = "This month expenses have decreased",
            percentage = "-2.4%",
            amountColor = Color(0xFFFF7B00),
            percentageColor = Color(0xFFFF7B00)
        )
        Spacer(modifier = Modifier.height(10.dp))
        FinancialCard(
            title = "Income",
            subtitle = "September 2023",
            amount = "+₹${incomeAmount}",
            description = "This month incomes have increased",
            percentage = "+10.9%",
            amountColor = Color(0xFF28A745),
            percentageColor = Color(0xFF28A745)
        )
        Spacer(modifier = Modifier.height(10.dp))
//        CustomLineChart(incomes, expenses, months)
        VicoLineChart()
        FinancialGoalsScreen()
    }
}

@Composable
fun FinancialCard(
    title: String,
    subtitle: String,
    amount: String,
    description: String,
    percentage: String,
    amountColor: Color,
    percentageColor: Color
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
            Text(
                text = amount,
                color = amountColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Row {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .weight(3f)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)

                ) {
                    Text(
                        text = percentage,
                        color = percentageColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )
                    androidx.compose.material3.Icon(
                        painter = painterResource(id = R.drawable.arrow_trend_down),
                        contentDescription = null,
                        tint = percentageColor,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )
                }

            }

        }
    }
}


//@Composable
//fun CustomLineChart(
//    incomes: List<Float>,
//    expenses: List<Float>,
//    months: List<String>
//) {
//    val maxIncome = incomes.maxOrNull() ?: 0f
//    val maxExpense = expenses.maxOrNull() ?: 0f
//    val maxY = maxOf(maxIncome, maxExpense)
//    Card(
//        shape = RoundedCornerShape(15.dp),
//        elevation = 4.dp,
//        modifier = Modifier.wrapContentHeight().padding(10.dp)
//    ){
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            Text("Total finances", style = MaterialTheme.typography.titleLarge)
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Canvas(modifier = Modifier.fillMaxWidth().height(200.dp)) {
//                val spacing = size.width / (incomes.size - 1)
//                val incomePath = Path().apply {
//                    moveTo(0f, size.height - (incomes[0] / maxY) * size.height)
//                    incomes.drop(1).forEachIndexed { index, value ->
//                        lineTo((index + 1) * spacing, size.height - (value / maxY) * size.height)
//                    }
//                }
//                val expensePath = Path().apply {
//                    moveTo(0f, size.height - (expenses[0] / maxY) * size.height)
//                    expenses.drop(1).forEachIndexed { index, value ->
//                        lineTo((index + 1) * spacing, size.height - (value / maxY) * size.height)
//                    }
//                }
//                drawPath(incomePath, Color.Blue)
//                drawPath(expensePath, Color.Red)
//
//                incomes.forEachIndexed { index, value ->
//                    drawCircle(
//                        Color.Blue,
//                        4.dp.toPx(),
//                        Offset(index * spacing, size.height - (value / maxY) * size.height)
//                    )
//                }
//                expenses.forEachIndexed { index, value ->
//                    drawCircle(
//                        Color.Red,
//                        4.dp.toPx(),
//                        Offset(index * spacing, size.height - (value / maxY) * size.height)
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                months.forEach {
//                    Text(it, fontSize = 12.sp)
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.Center
//            ) {
//                LegendItem(color = Color.Blue, text = "Incomes")
//                Spacer(modifier = Modifier.width(16.dp))
//                LegendItem(color = Color.Red, text = "Expenses")
//            }
//        }
//    }
//
//}

//@Composable
//fun LegendItem(color: Color, text: String) {
//    Row(verticalAlignment = Alignment.CenterVertically) {
//        Box(
//            modifier = Modifier
//                .size(12.dp)
//                .background(color)
//        )
//        Spacer(modifier = Modifier.width(8.dp))
//        Text(text)
//    }
//}
@Composable
fun VicoLineChart() {
    val incomeEntries = listOf(
        FloatEntry(0f, 10000f), FloatEntry(1f, 8000f), FloatEntry(2f, 6000f), FloatEntry(3f, 7000f),
        FloatEntry(4f, 9000f), FloatEntry(5f, 10000f), FloatEntry(6f, 12000f), FloatEntry(7f, 8000f),
        FloatEntry(8f, 11000f), FloatEntry(9f, 9500f), FloatEntry(10f, 10500f), FloatEntry(11f, 11500f)
    )

    val expenseEntries = listOf(
        FloatEntry(0f, 4000f), FloatEntry(1f, 3000f), FloatEntry(2f, 5000f), FloatEntry(3f, 6000f),
        FloatEntry(4f, 5000f), FloatEntry(5f, 4000f), FloatEntry(6f, 4500f), FloatEntry(7f, 4700f),
        FloatEntry(8f, 5200f), FloatEntry(9f, 4800f), FloatEntry(10f, 4900f), FloatEntry(11f, 5000f)
    )

    val chartEntryModelProducer = ChartEntryModelProducer(incomeEntries, expenseEntries)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Total finances", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        FinanceChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            incomes = listOf(8000f, 7000f, 9000f, 6000f, 8000f, 7000f, 9000f, 8000f, 9000f, 7000f, 8000f, 9000f),
            expenses = listOf(4000f, 3000f, 5000f, 2000f, 4000f, 3000f, 5000f, 3000f, 4000f, 2000f, 3000f, 5000f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec").forEach {
                Text(it, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            LegendItem(color = Color.Blue, text = "Incomes")
            Spacer(modifier = Modifier.width(16.dp))
            LegendItem(color = Color.Yellow, text = "Expenses")
        }
    }
}

@Composable
fun FinanceChart(
    modifier: Modifier = Modifier,
    incomes: List<Float>,
    expenses: List<Float>
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val maxIncome = incomes.maxOrNull() ?: 0f
        val maxExpense = expenses.maxOrNull() ?: 0f
        val maxValue = maxOf(maxIncome, maxExpense)
        val pointSpacing = width / (incomes.size - 1)

        val incomePath = Path().apply {
            moveTo(0f, height)
            for (i in incomes.indices) {
                val x = i * pointSpacing
                val y = height - (incomes[i] / maxValue) * height
                if (i == 0) {
                    lineTo(x, y)
                } else {
                    val prevX = (i - 1) * pointSpacing
                    val prevY = height - (incomes[i - 1] / maxValue) * height
                    cubicTo(
                        prevX + pointSpacing / 2, prevY,
                        x - pointSpacing / 2, y,
                        x, y
                    )
                }
            }
            lineTo(width, height)
            close()
        }

        val expensePath = Path().apply {
            moveTo(0f, height)
            for (i in expenses.indices) {
                val x = i * pointSpacing
                val y = height - (expenses[i] / maxValue) * height
                if (i == 0) {
                    lineTo(x, y)
                } else {
                    val prevX = (i - 1) * pointSpacing
                    val prevY = height - (expenses[i - 1] / maxValue) * height
                    cubicTo(
                        prevX + pointSpacing / 2, prevY,
                        x - pointSpacing / 2, y,
                        x, y
                    )
                }
            }
            lineTo(width, height)
            close()
        }

        drawPath(
            path = incomePath,
            color = Color(0xFFCCE5FF),
            style = Fill
        )
        drawPath(
            path = expensePath,
            color = Color(0xFFFFF2CC),
            style = Fill
        )
        drawPath(
            path = incomePath,
            color = Color(0xFF000080),
            style = Stroke(width = 4.dp.toPx())
        )
        drawPath(
            path = expensePath,
            color = Color(0xFFFFA500),
            style = Stroke(width = 4.dp.toPx())
        )
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}


data class Goal(
    val name: String,
    val current: Int,
    val target: Int
)

@Composable
fun FinancialGoalsCard(goals: List<Goal>) {
    Card(
        shape = RoundedCornerShape(15.dp),
        elevation = 4.dp,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Goals",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(16.dp))

            goals.forEach { goal ->
                GoalProgress(goal)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = "This month you saved 8% more than the previous month. Use the Savings service to achieve your goals faster",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun GoalProgress(goal: Goal) {
    Column {
        Text(
            text = goal.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = goal.current / goal.target.toFloat(),
            color = Color.Blue,
            backgroundColor = Color.LightGray,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "$${goal.current}")
            Text(text = "$${goal.target}")
        }
    }
}

@Composable
fun FinancialGoalsScreen() {
    val goals = listOf(
        Goal("Car", 7300, 10000),
        Goal("House", 56660, 270000),
        Goal("Vacation", 450, 6000),
        Goal("University", 17000, 18000)
    )

    FinancialGoalsCard(goals = goals)
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FinancialDashboard()
}
