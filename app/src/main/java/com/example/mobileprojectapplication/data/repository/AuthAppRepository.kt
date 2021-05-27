package com.example.mobileprojectapplication.data.repository


import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.mobileprojectapplication.utils.mainExecutor
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class AuthAppRepository(private val application: Application) {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()

    private val authLiveData: MutableLiveData<Task<AuthResult>> = MutableLiveData()
    private val registerLiveData: MutableLiveData<Task<AuthResult>> = MutableLiveData()

    val loggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(application.mainExecutor(),
                OnCompleteListener<AuthResult> { task ->
                    authLiveData.postValue(task)
                })
    }

    fun register(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(application.mainExecutor(),
                OnCompleteListener<AuthResult> { task ->
                    registerLiveData.postValue(task)
                })
    }

    fun logOut() {
        firebaseAuth.signOut()
        loggedOutLiveData.postValue(true)
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser> {
        userLiveData.postValue(firebaseAuth.currentUser)
        return userLiveData
    }
    fun getAuthLiveData(): MutableLiveData<Task<AuthResult>> {
        return authLiveData
    }
    fun getRegisterLiveData(): MutableLiveData<Task<AuthResult>> {
        return registerLiveData
    }
}