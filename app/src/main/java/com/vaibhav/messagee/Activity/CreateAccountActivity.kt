package com.vaibhav.messagee.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.vaibhav.messagee.R
import com.vaibhav.messagee.ModelClass.Users
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var diaglog: ProgressDialog
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private val pickImage = 100
    private var imageUri: Uri? = null
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var ImageUri: String
    private val status: String = "Here, I'am using Messagee Application"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        mAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        diaglog = ProgressDialog(this)

        tvloginaccount.setOnClickListener {
            Intent(this, LoginActivity::class.java).also {
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

            if (TextUtils.isEmpty(nameText) || TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText) || TextUtils.isEmpty(confirmPasswordText)){
                diaglog.dismiss()
                Toast.makeText(this,"Please Enter Valid Data",Toast.LENGTH_SHORT).show()
            }

            else if (!(emailText.matches(emailPattern.toRegex()))){
                diaglog.dismiss()
                etemail.setError("Please Enter Valid Email")
                Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_SHORT).show()
            }

            else if(passwordText.length < 8){
                diaglog.dismiss()
                Toast.makeText(this,"Password length cannot be less than 8",Toast.LENGTH_SHORT).show()
            }

            else if(!(passwordText.equals(confirmPasswordText))){
                diaglog.dismiss()
                Toast.makeText(this,"Confirm Password Doesn't match",Toast.LENGTH_SHORT).show()
            }

            else{
                signup(nameText,emailText,passwordText)
            }
        }

        recevier_profile_image.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery,pickImage)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && requestCode == pickImage){
            imageUri = data?.data!!
            recevier_profile_image.setImageURI(imageUri)
        }
    }

    private fun signup(nameText: String, emailText: String, passwordText: String) {

        mAuth.createUserWithEmailAndPassword(emailText, passwordText)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    var databaseReference = firebaseDatabase.getReference().child("user").child(mAuth.uid.toString())
                    var storageReference = firebaseStorage.getReference().child("upload").child(mAuth.uid.toString())
                    Log.d("DatabaseReference", "$databaseReference")
                    Log.d("StorageReference", "$storageReference")
                    Log.d("Image Uri", "$imageUri")

                    if (imageUri != null){
                        storageReference.putFile(imageUri!!).addOnCompleteListener {
                            if (it.isSuccessful) {
                                storageReference.downloadUrl.addOnSuccessListener {
                                    ImageUri = it.toString()
                                    val users = Users(mAuth.uid.toString(),nameText, emailText, ImageUri, status)
                                    databaseReference.setValue(users).addOnCompleteListener {
                                        if (it.isSuccessful){
                                            diaglog.dismiss()
                                            Intent(this, LoginActivity::class.java).also {
                                                startActivity(it)
                                                finish()
                                            }
                                        }

                                        else{
                                            diaglog.dismiss()
                                            Toast.makeText(this,"Some Error Occured", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                        }
                    }

                    else{
                        ImageUri = "https://firebasestorage.googleapis.com/v0/b/messagee-58243.appspot.com/o/user.png?alt=media&token=d9520778-b10c-4cff-9926-905a1983fc19"
                        val users = Users(mAuth.uid.toString(),nameText, emailText, ImageUri, status)
                        databaseReference.setValue(users).addOnCompleteListener {
                            if (it.isSuccessful){
                                diaglog.dismiss()
                                Intent(this, LoginActivity::class.java).also {
                                    startActivity(it)
                                    finish()
                                }
                            }

                            else{
                                diaglog.dismiss()
                                Toast.makeText(this,"Some Error Occured", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                } else {
                    diaglog.dismiss()
                    Toast.makeText(this,"Some Error occured",Toast.LENGTH_SHORT).show()
                }
            }
    }
}