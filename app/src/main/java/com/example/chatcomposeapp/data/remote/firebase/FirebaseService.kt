package com.example.chatcomposeapp.data.remote.firebase

import com.example.chatcomposeapp.data.remote.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FirebaseService @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun getUser(userId: String): User? {
        val document = firestore.collection("users").document(userId).get().await()
        return document.toObject(User::class.java)
    }

    suspend fun createUser(user: User) {
        firestore.collection("users").document(user.id).set(user).await()
    }

}