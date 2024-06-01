package com.example.chatcomposeapp.presentation.viewmodel

import android.util.Log
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
                            Log.d("AuthViewModel", "Sign in successful")
                            _authState.value = FirebaseAuthState.Success
                        } else {
                            Log.e("AuthViewModel", "Sign in failed: ${task.exception?.message}")
                            _authState.value = FirebaseAuthState.Error(task.exception?.message ?: "Unknown error")
                        }
                    }
            } catch (ex: Exception) {
                Log.e("AuthViewModel", "Sign in exception: ${ex.message}")
                _authState.value = FirebaseAuthState.Error(ex.message ?: "Unknown error")
            }
        }
    }


    fun createUser(user: User) {
        viewModelScope.launch {
            _authState.value = FirebaseAuthState.Loading
            try {
                firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val newUser = task.result?.user
                            if (newUser != null) {
                                Log.d("AuthViewModel", "User created successfully with UID: ${newUser.uid}")
                                viewModelScope.launch {
                                    try {
                                        firebaseService.createUser(user.copy(id = newUser.uid))
                                        Log.d("AuthViewModel", "User data added to Firestore")
                                        _authState.value = FirebaseAuthState.Success
                                    } catch (e: Exception) {
                                        Log.e("AuthViewModel", "Error adding user data to Firestore: ${e.message}")
                                        _authState.value = FirebaseAuthState.Error(e.message ?: "Unknown error")
                                    }
                                }
                            } else {
                                Log.e("AuthViewModel", "User creation failed: no user info returned")
                                _authState.value = FirebaseAuthState.Error("User creation failed: no user info returned")
                            }
                        } else {
                            Log.e("AuthViewModel", "User creation failed: ${task.exception?.message}")
                            if (task.exception != null) {
                                Log.e("AuthViewModel", "Exception: ", task.exception)
                            }
                            _authState.value = FirebaseAuthState.Error(task.exception?.message ?: "Unknown error")
                        }
                    }
            } catch (ex: Exception) {
                Log.e("AuthViewModel", "User creation exception: ${ex.message}")
                _authState.value = FirebaseAuthState.Error(ex.message ?: "Unknown error")
            }
        }
    }

     /*
    fun createUser(user: User) {
        viewModelScope.launch {
            _authState.value = FirebaseAuthState.Loading
            try {
                firebaseService.createUser(user)
                _authState.value = FirebaseAuthState.Success
            } catch (ex: Exception) {
                _authState.value = FirebaseAuthState.Error(ex.message ?: "Unknown error")
                Log.e("Create user Error", ex.message.toString())
            }
        }
    }

      */



    fun getUser(userId: String) {
        viewModelScope.launch {
            _authState.value = FirebaseAuthState.Loading
            try {
                val user = firebaseService.getUser(userId)
                if (user != null) {
                    Log.d("AuthViewModel", "User loaded successfully")
                    _authState.value = FirebaseAuthState.UserLoaded(user)
                } else {
                    Log.e("AuthViewModel", "User not found")
                    _authState.value = FirebaseAuthState.Error("User not found")
                }
            } catch (ex: Exception) {
                Log.e("AuthViewModel", "Error loading user: ${ex.message}")
                _authState.value = FirebaseAuthState.Error(ex.message ?: "Unknown error")
            }
        }
    }



}