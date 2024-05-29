package com.example.chatcomposeapp.di

import com.example.chatcomposeapp.data.remote.firebase.FirebaseService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseService(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): FirebaseService {
        return FirebaseService(firebaseAuth, firestore)
    }

}