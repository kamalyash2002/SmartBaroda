package com.uphar.smartbaroda.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.uphar.bussinesss.domain.Utils.state.DataState
import com.uphar.bussinesss.domain.data.login.RegisterCredentials
import com.uphar.smartbaroda.MainActivity
import com.uphar.smartbaroda.MainViewModel
import com.uphar.smartbaroda.R


@Composable
fun RegisterForm(navController: NavController) {
    Surface {
        var credentials by remember { mutableStateOf(RegisterCredentials()) }
        val context = LocalContext.current
        val viewModel: MainViewModel = hiltViewModel()
        var showAlert by remember { mutableStateOf(false) }
        val registerTokenQueryDataState by viewModel.registerTokenQueryDataState.collectAsState()
        when (registerTokenQueryDataState) {
            is DataState.Loading -> {
                // Show loading indicator
            }
            is DataState.Success -> {
                LaunchedEffect(Unit) {
                    if((registerTokenQueryDataState as DataState.Success).data.isEmpty())
                    {
                        showAlert = true
                        //wrong credntail alert dialgoue
                    }
                    else{
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                        (context as Activity).finish()
                    }

                }
            }
            is DataState.Error -> {
                showAlert = true
                val errorMessage = (registerTokenQueryDataState as DataState.Error).exception.message
                // Show error message
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
                .verticalScroll(rememberScrollState()) //
        ) {
            Image(
                painter = painterResource(R.drawable.bob_logo),
                contentDescription = "BOB Logo",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))
            RegisterField(
                label = "Account Number",
                value = credentials.accNo,
                onChange = { data -> credentials = credentials.copy(accNo = data) },
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Number
            )
            RegisterField(
                label = "Username",
                value = credentials.username,
                onChange = { data -> credentials = credentials.copy(username = data) },
                modifier = Modifier.fillMaxWidth()
            )
            RegisterField(
                label = "PAN Card",
                value = credentials.panCard,
                onChange = { data -> credentials = credentials.copy(panCard = data) },
                modifier = Modifier.fillMaxWidth()
            )
            RegisterField(
                label = "CIBIL Score",
                value = credentials.cibilScore,
                onChange = { data -> credentials = credentials.copy(cibilScore = data) },
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Number
            )
            RegisterField(
                label = "Account Type",
                value = credentials.type,
                onChange = { data -> credentials = credentials.copy(type = data) },
                modifier = Modifier.fillMaxWidth()
            )
            AccountTypeSelection(
                selectedType = credentials.type,
                onAccountTypeChange = { type ->
                    credentials = credentials.copy(type = type)
                }
            )
            if (credentials.type == "Current") {
                RegisterField(
                    label = "GST No",
                    value = credentials.cibilScore,
                    onChange = { data -> credentials = credentials.copy(cibilScore = data) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Number
                )
            }

            PasswordField(
                label = "Password",
                value = credentials.password,
                onChange = { data -> credentials = credentials.copy(password = data) },
                modifier = Modifier.fillMaxWidth(),
                submit = {

                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            PasswordField(
                label = "Confirm Password",
                value = credentials.confirmPassword,
                onChange = { data -> credentials = credentials.copy(confirmPassword = data) },
                modifier = Modifier.fillMaxWidth(),
                submit = {

                }
            )
            LoginButton(navController = navController)
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
//                    context.startActivity(Intent(context, MainActivity::class.java))
//                    (context as Activity).finish()
                    viewModel.registerUser(credentials)
                },
                enabled = credentials.password.isNotEmpty() && credentials.password == credentials.confirmPassword,
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register")
            }

        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}
@Composable
fun LoginButton(navController: NavController) {
    Row(
        Modifier
            .fillMaxWidth() // Ensure the Row takes the full width
            .padding(4.dp),
        horizontalArrangement = Arrangement.End, // Align items to the end (right)
        verticalAlignment = Alignment.CenterVertically // Align items vertically center
    ) {
        Text(
            text = "Login",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .clickable {
                    navController.navigate("login")
                }
                .padding(16.dp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun AccountTypeSelection(
    selectedType: String,
    onAccountTypeChange: (String) -> Unit
) {
    val accountTypes = listOf("Savings", "Current")
    val lightPurple = Color(0xFFD1C4E9)  // Light purple color

    Column {
//        Text(text = "Account Type", style = MaterialTheme.typography.headlineLarge)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            accountTypes.forEach { type ->
                Chip(
                    onClick = { onAccountTypeChange(type) },
                    colors = ChipDefaults.chipColors(
                        contentColor = if (selectedType == type) MaterialTheme.colorScheme.onPrimary else Color.Black
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(text = type)
                }
            }
        }
    }
}

@Composable
fun RegisterField(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text
) {

    val focusManager = LocalFocusManager.current
    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Person,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }

    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = keyboardType),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = VisualTransformation.None
    )
    Spacer(modifier = Modifier.height(20.dp))
}