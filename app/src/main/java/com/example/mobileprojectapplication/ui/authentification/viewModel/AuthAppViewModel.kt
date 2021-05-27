package com.example.mobileprojectapplication.ui.authentification.viewModel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mobileprojectapplication.data.repository.AuthAppRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser


class AuthAppViewModel(application: Application) :
    AndroidViewModel(application) {
    private val authAppRepository: AuthAppRepository = AuthAppRepository(application)
    val userLiveData: MutableLiveData<FirebaseUser> = authAppRepository.getUserLiveData()
    val authLiveData : MutableLiveData<Task<AuthResult>> = authAppRepository.getAuthLiveData()
    val registerLiveData : MutableLiveData<Task<AuthResult>> = authAppRepository.getRegisterLiveData()

    fun login(email: String, password: String) {
        authAppRepository.login(email, password)
    }

    fun register(email: String, password: String) {
        authAppRepository.register(email, password)
    }
    fun logOut() {
        authAppRepository.logOut()
    }
}