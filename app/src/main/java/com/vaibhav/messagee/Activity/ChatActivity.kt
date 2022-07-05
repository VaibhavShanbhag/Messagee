package com.vaibhav.messagee.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.vaibhav.messagee.Adapter.MessageListAdapter
import com.vaibhav.messagee.ModelClass.MessageModel
import com.vaibhav.messagee.R
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {
    private lateinit var ReceiverImage: String
    private lateinit var ReceiverName: String
    private lateinit var ReceiverUid: String
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var senderUid: String
    private lateinit var senderRoom: String
    private lateinit var receiverRoom: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatReference: DatabaseReference
    private lateinit var messageArrayList: ArrayList<MessageModel>
    private lateinit var adapter: MessageListAdapter
    companion object{
        lateinit var senderImage: String
        lateinit var recevierImage: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        firebaseDatabase = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        recyclerView = findViewById(R.id.rvchatusers)

        messageArrayList = ArrayList<MessageModel>()

        adapter = MessageListAdapter(this,messageArrayList)
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd
        recyclerView.layoutManager = linearLayoutManager

        recyclerView.adapter = adapter

        ReceiverImage = intent.getStringExtra("receiverImage").toString()
        ReceiverName = intent.getStringExtra("name").toString()
        ReceiverUid = intent.getStringExtra("uid").toString()

        senderUid = auth.uid.toString()
        recevierImage = ReceiverImage

        senderRoom = senderUid+ReceiverUid
        receiverRoom = ReceiverUid+senderUid

        backImage.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        Glide.with(this)
            .load(ReceiverImage)
            .into(recevier_profile_image)

        receiver_name.text = ReceiverName

        databaseReference = firebaseDatabase.getReference().child("user").child(senderUid)
        chatReference = firebaseDatabase.getReference().child("chats").child(senderRoom).child("messages")

        chatReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messageArrayList.clear()
                for (datasnapshot: DataSnapshot in snapshot.children){
                    val messageModel = datasnapshot.getValue(MessageModel::class.java)
                    messageArrayList.add(messageModel!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                 senderImage = snapshot.child("imageUri").getValue().toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        ivsendmessage.setOnClickListener {
            val message:String = etmessage.text.toString()
            if(message.isEmpty()){
                Toast.makeText(this,"Please Enter Message", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            etmessage.text.clear()
            val date: Date = Date()
            val messageModel = MessageModel(message,senderUid,date.time)
            firebaseDatabase.getReference().child("chats").child(senderRoom).child("messages").push().setValue(messageModel).addOnCompleteListener {
                firebaseDatabase.getReference().child("chats").child(receiverRoom).child("messages").push().setValue(messageModel).addOnCompleteListener {

                }
            }

        }
    }
}