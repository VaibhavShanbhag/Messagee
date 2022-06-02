package com.vaibhav.messagee

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var diaglog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        mAuth = FirebaseAuth.getInstance()
        diaglog = ProgressDialog(this)

        tvloginaccount.setOnClickListener {
            Intent(this,LoginActivity::class.java).also {
                startActivity(it)
            }
        }

        createaccountbutton.setOnClickListener {
            diaglog.setMessage("Creating New Account..")
            diaglog.show()

            val nameText: String = etname.text.toString()
            val emailText: String = etemail.text.toString()
            val passwordText: String = etpassword.text.toString()
            val confirmPasswordText: String = etconfirmpass.text.toString()

            if(passwordText.length < 8){
                diaglog.dismiss()
                Toast.makeText(this,"Password length cannot be less than 8",Toast.LENGTH_SHORT).show()
            }

            else if(!(passwordText.equals(confirmPasswordText))){
                diaglog.dismiss()
                Toast.makeText(this,"Confirm Password Doesn't match",Toast.LENGTH_SHORT).show()
            }

            else
                signup(nameText,emailText,passwordText)
        }

    }

    private fun signup(nameText: String, emailText: String, passwordText: String) {
        mAuth.createUserWithEmailAndPassword(emailText, passwordText)
            .addOnCompleteListener(this) { task ->
                diaglog.dismiss()
                if (task.isSuccessful) {
                    Intent(this, LoginActivity::class.java).also {
                        startActivity(it)
                    }

                } else {
                    Toast.makeText(this,"Some Error occured",Toast.LENGTH_SHORT).show()
                }
            }
    }
}