package com.example.chatcomposeapp.presentation.viewmodel.state

import com.example.chatcomposeapp.data.remote.model.User

sealed class FirebaseAuthState {
    data object Idle: FirebaseAuthState()
    data object Loading: FirebaseAuthState()
    data object Success: FirebaseAuthState()
    data class Error(val message: String): FirebaseAuthState()
    data class UserLoaded(val user: User): FirebaseAuthState()
}