package com.vaibhav.messagee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

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
                    userArrayList.add(users!!)
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




    }
}