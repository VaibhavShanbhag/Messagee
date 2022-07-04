package com.vaibhav.messagee.Activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.vaibhav.messagee.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.etemail
import kotlinx.android.synthetic.main.activity_login.etpassword

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var diaglog: ProgressDialog
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        diaglog = ProgressDialog(this)

        tvcreateaccount.setOnClickListener {
            Intent(this, CreateAccountActivity::class.java).also {
                startActivity(it)
            }
        }

        login_button.setOnClickListener {
            diaglog.setMessage("Trying to login. Please wait..")
            diaglog.show()

            val email: String = etemail.text.toString()
            val password: String = etpassword.text.toString()

            if(TextUtils.isEmpty(email)){
                diaglog.dismiss()
                etemail.setError("Email is Empty")
            }

            else if(TextUtils.isEmpty(password)){
                diaglog.dismiss()
                etpassword.setError("Password is Empty")
            }

            else if(!email.matches(emailPattern.toRegex())){
                diaglog.dismiss()
                etemail.setError("Please Enter Valid Email")
                Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_SHORT).show()
            }

            else
            login(email,password)
        }
    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                diaglog.dismiss()
                if (task.isSuccessful) {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()

                } else {
                    Toast.makeText(this,"Invalid Email Id or Password", Toast.LENGTH_SHORT).show()
                }
            }
    }
}