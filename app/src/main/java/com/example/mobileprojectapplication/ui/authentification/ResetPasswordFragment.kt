package com.example.mobileprojectapplication.ui.authentification

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mobileprojectapplication.R
import com.example.mobileprojectapplication.utils.displayMessage
import com.example.mobileprojectapplication.utils.isEmailValid
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_reset_password.*


class ResetPasswordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        returnToLoginPage.setOnClickListener {
            findNavController().navigate(R.id.action_nav_reset_password_to_nav_login)
        }

        resetPasswordButton.setOnClickListener {
            val email = editTextEmail.text.toString()
            if (email.isEmpty()) {
                view?.displayMessage(getString(R.string.mail_or_password_not_entered))
            } else if (!email.isEmailValid()) {
                view?.displayMessage(getString(R.string.error_invalid_email))
            } else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            view?.displayMessage(getString(R.string.reset_password__email_sended,email))
                        } else {
                            view?.displayMessage(getString(R.string.cannot_find_user_with_this_email,email))

                        }
                    }
            }
        }
    }

}