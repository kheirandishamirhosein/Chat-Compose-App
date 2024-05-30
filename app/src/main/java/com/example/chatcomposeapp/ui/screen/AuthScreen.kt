package com.example.chatcomposeapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatcomposeapp.data.remote.model.User
import com.example.chatcomposeapp.presentation.viewmodel.AuthViewModel
import com.example.chatcomposeapp.presentation.viewmodel.state.FirebaseAuthState

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isSignUp by remember { mutableStateOf(false) }

    val scaffoldState = rememberScaffoldState()
    val snackbarHostState = remember { SnackbarHostState() }

    val authState by viewModel.authState.collectAsState()

    when (authState) {
        is FirebaseAuthState.Success -> {
            onLoginSuccess()
        }

        is FirebaseAuthState.Error -> {
            val errorMessage = (authState as FirebaseAuthState.Error).message
            LaunchedEffect(scaffoldState.snackbarHostState) {
                snackbarHostState.showSnackbar(errorMessage)
            }
        }

        else -> {}
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Show CircularProgressIndicator if loading
                if (authState is FirebaseAuthState.Loading) {
                    CircularProgressIndicator()
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isSignUp) "Sign Up" else "Login",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (isSignUp) {
                            viewModel.createUser(
                                User(
                                    id = 0,
                                    username = "",
                                    email = email,
                                    password = password
                                )
                            )
                        } else {
                            viewModel.singIn(
                                User(
                                    id = 0,
                                    username = "",
                                    email = email,
                                    password = password
                                )
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = if (isSignUp) "Sign Up" else "Login")
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = { isSignUp = !isSignUp }) {
                    Text(text = if (isSignUp) "Already have an account? Login" else "Don't have an account? Sign Up")
                }
            }
        }
    )

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier.padding(top = 16.dp, start = 8.dp, end = 8.dp)
    ) { snackbarData ->
        Snackbar(
            snackbarData = snackbarData,
            modifier = Modifier.padding(8.dp)
        )
    }
}





