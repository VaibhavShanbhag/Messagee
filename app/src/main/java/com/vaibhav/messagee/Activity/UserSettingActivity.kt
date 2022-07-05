package com.vaibhav.messagee.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.vaibhav.messagee.ModelClass.Users
import com.vaibhav.messagee.R
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_user_setting.*
import kotlinx.android.synthetic.main.activity_user_setting.etname
import kotlinx.android.synthetic.main.user_item_list.*
import kotlinx.android.synthetic.main.user_item_list.recevier_profile_image

class UserSettingActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var diaglog: ProgressDialog
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private val pickImage: Int = 100
    private var imageUri: Uri? = null
    private lateinit var email: String
    private lateinit var storageReference: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_setting)

        etemailid.isEnabled = false

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        diaglog = ProgressDialog(this)

        storageReference = storage.getReference().child("upload").child(auth.uid.toString())
        databaseReference = database.getReference().child("user").child(auth.uid.toString())
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                email = snapshot.child("email").getValue().toString()
                val status: String = snapshot.child("status").getValue().toString()
                val name: String = snapshot.child("name").getValue().toString()
                val imageUri: String = snapshot.child("imageUri").getValue().toString()

                etemailid.setText(email)
                etstatus.setText(status)
                etname.setText(name)

                Glide.with(this@UserSettingActivity)
                    .load(imageUri)
                    .into(profile_image)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        profile_image.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery,pickImage)
        }

        backImage.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
            finish()
        }

        updateButton.setOnClickListener {
            diaglog.setMessage("Please Wait...")
            diaglog.show()
            val name: String = etname.text.toString()
            val status: String = etstatus.text.toString()

            if(imageUri != null){
                storageReference.putFile(imageUri!!).addOnCompleteListener{
                    storageReference.downloadUrl.addOnSuccessListener {
                        val imageUri = it.toString()
                        val users = Users(auth.uid.toString(),name,email,imageUri,status)
                        databaseReference.setValue(users).addOnCompleteListener {
                            diaglog.dismiss()
                            if(it.isSuccessful){
                                Toast.makeText(this,"Data Updated Successfully", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this,HomeActivity::class.java))
                                finishAffinity()
                                finish()
                            }

                            else{
                                Toast.makeText(this, "Some Error Occured", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            else{
                storageReference.downloadUrl.addOnSuccessListener {
                    val imageUri = it.toString()
                    val users = Users(auth.uid.toString(),name,email,imageUri,status)
                    databaseReference.setValue(users).addOnCompleteListener {
                        diaglog.dismiss()
                        if(it.isSuccessful){
                            startActivity(Intent(this,HomeActivity::class.java))
                            finishAffinity()
                            finish()
                        }

                        else{
                            Toast.makeText(this, "Some Error Occured", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == pickImage){
            imageUri = data?.data!!
            profile_image.setImageURI(imageUri)
        }
    }
}