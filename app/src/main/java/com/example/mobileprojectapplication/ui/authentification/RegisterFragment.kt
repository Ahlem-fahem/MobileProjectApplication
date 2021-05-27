package com.example.mobileprojectapplication.ui.authentification

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mobileprojectapplication.MainActivity
import com.example.mobileprojectapplication.R
import com.example.mobileprojectapplication.ui.authentification.viewModel.AuthAppViewModel
import com.example.mobileprojectapplication.utils.displayMessage
import com.example.mobileprojectapplication.utils.isEmailValid
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.editTextEmail
import kotlinx.android.synthetic.main.fragment_register.editTextPassword
import kotlinx.android.synthetic.main.fragment_register.loginProgress

class RegisterFragment : Fragment() {

    private lateinit var authAppViewModel : AuthAppViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_register, container, false)
        authAppViewModel =
                ViewModelProvider(this).get(AuthAppViewModel::class.java)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        registerButton.setOnClickListener {
            onUserRegister()
        }
        returnToLoginPage.setOnClickListener {
            findNavController().navigate(R.id.action_nav_register_to_nav_login)
        }
    }
    private fun onUserRegister(){
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
        val repeatPassword = editTextRepeatPassword.text.toString()

        if (email.isEmpty()  || password.isEmpty()) {
            view?.displayMessage(getString(R.string.mail_or_password_not_entered))
        }  else if (!email.isEmailValid()){
            view?.displayMessage(getString(R.string.error_invalid_email))
        }  else if (password != repeatPassword){
            view?.displayMessage(getString(R.string.wrong_password_repeat))
        }  else {
            loginProgress.visibility = View.VISIBLE
            authAppViewModel.register(email, password)
            authAppViewModel.registerLiveData.observe(viewLifecycleOwner,  Observer { task ->
                if(task.isSuccessful){
                    onUserLogin(email,password)
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Registration Failure: " + task.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    loginProgress.visibility = View.GONE
                }

            })
        }
    }
    private fun onUserLogin(email : String, password : String){
        authAppViewModel.login(email, password)
        authAppViewModel.authLiveData.observe(viewLifecycleOwner,  Observer { task ->
            if(task.isSuccessful){
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Login Failure: " + task.exception?.message,
                    Toast.LENGTH_SHORT
                ).show()
                loginProgress.visibility = View.GONE

            }
        })
    }
}