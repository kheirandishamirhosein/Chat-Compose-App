package com.example.chatcomposeapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatcomposeapp.data.remote.firebase.FirebaseService
import com.example.chatcomposeapp.data.remote.model.User
import com.example.chatcomposeapp.presentation.viewmodel.state.FirebaseAuthState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseService: FirebaseService
) : ViewModel() {

    private val _authState = MutableStateFlow<FirebaseAuthState>(FirebaseAuthState.Idle)
    val authState: StateFlow<FirebaseAuthState> = _authState

    fun singIn(user: User) {
        viewModelScope.launch {
            _authState.value = FirebaseAuthState.Loading
            try {
                firebaseAuth.signInWithEmailAndPassword(user.email, user.password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _authState.value = FirebaseAuthState.Success
                        } else {
                            _authState.value =
                                FirebaseAuthState.Error(task.exception?.message ?: "Unknown error")
                        }

                    }
            } catch (ex: Exception) {
                _authState.value = FirebaseAuthState.Error(ex.message ?: "Unknown error")
            }
        }
    }

    fun createUser(user: User) {
        viewModelScope.launch {
            _authState.value = FirebaseAuthState.Loading
            try {
                firebaseService.createUser(user)
                _authState.value = FirebaseAuthState.Success
            } catch (ex: Exception) {
                _authState.value = FirebaseAuthState.Error(ex.message ?: "Unknown error")
            }
        }
    }

    fun getUser(userId: String) {
        viewModelScope.launch {
            _authState.value = FirebaseAuthState.Loading
            try {
                val user = firebaseService.getUser(userId)
                if (user != null) {
                    _authState.value = FirebaseAuthState.UserLoaded(user)
                } else {
                    _authState.value = FirebaseAuthState.Error("User not found")
                }
            } catch (ex: Exception) {
                _authState.value = FirebaseAuthState.Error(ex.message ?: "Unknown error")
            }
        }
    }


}