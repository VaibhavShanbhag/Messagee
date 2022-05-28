package com.vaibhav.messagee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tvforgotpass.setOnClickListener {
            Intent(this,ForgotPasswordActivity::class.java).also {
                startActivity(it)
            }
        }

        tvcreateaccount.setOnClickListener {
            Intent(this,CreateAccountActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}