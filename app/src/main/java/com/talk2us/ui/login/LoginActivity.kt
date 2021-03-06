package com.talk2us.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.talk2us.R
import com.talk2us.ui.chat.ChatActivity
import com.talk2us.utils.Constants
import com.talk2us.utils.PrefManager
import com.talk2us.utils.Utils


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()

        if (PrefManager.getBoolean(Constants.FIRST_TIME, true)) {
            startActivity(Intent(applicationContext, WelcomeActivity::class.java))
            finish()
        }

        loginViewModel = ViewModelProviders.of(this)
            .get(LoginViewModel::class.java)
        loginViewModel.codeSent.observe(this, Observer {
            if (it) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ConfirmOtpFragment()).commit()
            }
        })
        loginViewModel.credential.observe(this, Observer {
            signInWithPhoneCredential(it)
        })

        supportFragmentManager.beginTransaction().replace(R.id.container, SendOtpFragment()).commit()
    }

    private fun signInWithPhoneCredential(it: PhoneAuthCredential) {
        mAuth.signInWithCredential(it).addOnCompleteListener {
            if (it.isSuccessful) {
                PrefManager.putString(Constants.PHONE_NUMBER, loginViewModel.phone)
                startActivity(Intent(applicationContext,ChatActivity::class.java))
                finish()
            } else {
                Utils.toast(it.exception.toString())
                if (it.exception is FirebaseAuthInvalidCredentialsException) {
                    Utils.toast("Invalid OTP")
                }
            }
        }
    }
}