package com.example.fitness_app

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(nav: NavController) {
    val ctx = LocalContext.current as ComponentActivity
    val vm: AuthViewModel = viewModel(viewModelStoreOwner = ctx)
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loading by vm.loading.collectAsState()
    val err by vm.error.collectAsState()

    AuthScaffold(title = "Login") {
        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = password, onValueChange = { password = it },
                label = { Text("Password") }, singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(12.dp))
            TextButton(onClick = { nav.navigate(Routes.RESET) }) { Text("Forgot Password") }
        }

        Button(
            onClick = {
                scope.launch {
                    vm.login(email, password) {
                        nav.navigate(Routes.HOME) { popUpTo(0) { inclusive = true } }
                    }
                }
            },
            enabled = !loading,
            modifier = Modifier.fillMaxWidth()
        ) { Text(if (loading) "Please wait..." else "Login") }

        if (err != null) {
            Text(err ?: "", color = MaterialTheme.colorScheme.error)
            LaunchedEffect(err) { vm.consumeError() }
        }

        TextButton(
            onClick = { nav.navigate(Routes.REGISTER) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) { Text("Don't have an account? Register") }
    }
}

@Composable
fun RegisterScreen(nav: NavController) {
    val ctx = LocalContext.current as ComponentActivity
    val vm: AuthViewModel = viewModel(viewModelStoreOwner = ctx)
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    val loading by vm.loading.collectAsState()
    val err by vm.error.collectAsState()

    AuthScaffold(title = "Create an account") {
        OutlinedTextField(name, { name = it }, label = { Text("Name") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(email, { email = it }, label = { Text("Email") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(password, { password = it }, label = { Text("Password") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(weight, { weight = it.filter { c -> c.isDigit() } }, label = { Text("Weight (kg)") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())

        Button(
            onClick = {
                val w = weight.toIntOrNull() ?: 70
                scope.launch {
                    vm.register(name, email, password, w) {
                        nav.navigate(Routes.HOME) { popUpTo(0) { inclusive = true } }
                    }
                }
            },
            enabled = !loading,
            modifier = Modifier.fillMaxWidth()
        ) { Text(if (loading) "Please wait..." else "Register") }

        if (err != null) {
            Text(err ?: "", color = MaterialTheme.colorScheme.error)
            LaunchedEffect(err) { vm.consumeError() }
        }

        TextButton(
            onClick = { nav.navigate(Routes.LOGIN) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) { Text("Already have an account ? Login") }
    }
}

@Composable
fun ResetPasswordScreen(nav: NavController) {
    val ctx = LocalContext.current as ComponentActivity
    val vm: AuthViewModel = viewModel(viewModelStoreOwner = ctx)
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    val loading by vm.loading.collectAsState()

    AuthScaffold(title = "Reset Password") {
        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { scope.launch { vm.sendReset(email) } },
            enabled = !loading,
            modifier = Modifier.fillMaxWidth()
        ) { Text(if (loading) "Sending..." else "Send Reset Link") }

        TextButton(
            onClick = { nav.navigate(Routes.LOGIN) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) { Text("Remember your password? Back to login") }
    }
}

/* küçük ortak scaffold */
@Composable
private fun AuthScaffold(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.9f),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    when (title) {
                        "Login" -> "Enter your email and password to access your account"
                        "Reset Password" -> "Enter your email address and we'll send you a link to reset your password"
                        else -> "Enter your details to create your fitness tracker account"
                    },
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(4.dp))
                content()
            }
        }
    }
}
