package com.example.chatcomposeapp.data

import com.google.firebase.auth.FirebaseAuth


val auth = FirebaseAuth.getInstance()

fun signInWithEmail(email: String, password: String, onResult: (Boolean) -> Unit) {
    auth.signInWithEmailAndPassword(email,password)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                onResult(true)
            } else {
                onResult(false)
            }
        }
}

fun signUpWithEmail(email: String, password: String, onResult: (Boolean) -> Unit) {
    auth.createUserWithEmailAndPassword(email,password)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                onResult(true)
            } else {
                onResult(false)
            }
        }
}