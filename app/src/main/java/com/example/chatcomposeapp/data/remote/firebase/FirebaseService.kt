package com.example.chatcomposeapp.data.remote.firebase

import com.example.chatcomposeapp.data.remote.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseService {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun getUser(userId: String): User? {
        val document = firestore.collection("users").document(userId).get().await()
        return document.toObject(User::class.java)
    }

    suspend fun createUser(user: User) {
        firestore.collection("users").document().set(user).await()
    }

}