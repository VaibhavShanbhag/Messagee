package com.vaibhav.messagee

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var diaglog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        diaglog = ProgressDialog(this)

        tvcreateaccount.setOnClickListener {
            Intent(this,CreateAccountActivity::class.java).also {
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

            else
            login(email,password)
        }
    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                diaglog.dismiss()
                if (task.isSuccessful) {
                    Toast.makeText(this,"Logged In",Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this,"User Does Not Exist", Toast.LENGTH_SHORT).show()
                }
            }
    }
}