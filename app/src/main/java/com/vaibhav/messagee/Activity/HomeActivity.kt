package com.vaibhav.messagee.Activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.vaibhav.messagee.R
import com.vaibhav.messagee.Adapter.UserListAdapter
import com.vaibhav.messagee.ModelClass.Users
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserListAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userArrayList: ArrayList<Users>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference().child("user")
        userArrayList = ArrayList<Users>()

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(datasnapshot: DataSnapshot in snapshot.children){
                    val users = datasnapshot.getValue(Users::class.java)
                    if (FirebaseAuth.getInstance().currentUser?.uid.toString().equals(users?.uid)){
                        continue
                    }

                    else{
                        userArrayList.add(users!!)
                    }

                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


        recyclerView = findViewById(R.id.rvuserview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UserListAdapter(this,userArrayList)
        recyclerView.adapter = adapter

        ivlogout.setOnClickListener {
            var dialog = Dialog(this, R.style.Dialoge)
            dialog.setContentView(R.layout.dialog_layout)
            val tvnobtn: TextView = dialog.findViewById(R.id.tvnobtn)
            val tvyesbtn: TextView = dialog.findViewById(R.id.tvyesbtn)
            tvnobtn.setOnClickListener {
                dialog.dismiss()
            }
            tvyesbtn.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            dialog.show()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
//        finishAffinity();
//        finish();
//        moveTaskToBack(true);
    }
}