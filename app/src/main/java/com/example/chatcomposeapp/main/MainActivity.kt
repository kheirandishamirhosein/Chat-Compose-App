package com.example.chatcomposeapp.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatcomposeapp.ui.screen.AuthScreen
import com.example.chatcomposeapp.ui.screen.HomeScreen
import com.example.chatcomposeapp.ui.theme.ChatComposeAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    //private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatComposeAppTheme {
                ChatAppNavHost()
            }
        }
        //
        //auth = FirebaseAuth.getInstance()
        //createUserWithEmail("test@example.com", "password1234")
    }

    /*
    private fun createUserWithEmail(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    Log.d("FirebaseAuth", "createUserWithEmail:success, User ID: ${user?.uid}")
                } else {
                    Log.w("FirebaseAuth", "createUserWithEmail:failure", task.exception)

                }
            }
    }

     */

}

@Composable
fun ChatAppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "auth") {
        composable("auth") {
            AuthScreen(
                onLoginSuccess = {navController.navigate("home")}
            )
        }
        composable("home") {
            HomeScreen()
        }
    }
}

