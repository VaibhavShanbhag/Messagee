package com.vaibhav.messagee.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.vaibhav.messagee.R
import kotlinx.android.synthetic.main.activity_on_board.*

class OnBoardActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_board)

        mAuth = FirebaseAuth.getInstance()

        if (mAuth.currentUser != null){
            startActivity(Intent(this, HomeActivity::class.java))
        }

        nextButton.setOnClickListener {
            loginActivity()
        }

        skipButton.setOnClickListener {
            loginActivity()
        }
    }

    private fun loginActivity(){
        Intent(this, LoginActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
}