package com.vaibhav.messagee.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.vaibhav.messagee.Activity.ChatActivity
import com.vaibhav.messagee.ModelClass.MessageModel
import com.vaibhav.messagee.R
import kotlinx.android.synthetic.main.receiver_layout_item.view.*
import kotlinx.android.synthetic.main.sender_layout_item.view.*

private const val ITEM_SEND = 1
private const val ITEM_RECEIVE = 2

class MessageListAdapter(var context: Context?, var messageArrayList: ArrayList<MessageModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    class SenderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(messageModel: MessageModel, context: Context){
            Glide.with(context)
                .load(ChatActivity.senderImage)
                .into(itemView.sender_profile_image)
            itemView.tvsendertext.text = messageModel.message
        }
    }
    class ReceiverViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(messageModel: MessageModel, context: Context){
            Glide.with(context)
                .load(ChatActivity.recevierImage)
                .into(itemView.receiver_profile_image)
            itemView.tvreceivertext.text = messageModel.message
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        if (viewType == ITEM_SEND){
            val view = LayoutInflater.from(context).inflate(R.layout.sender_layout_item,parent,false)
            return SenderViewHolder(view)
        }

        else{
            val view = LayoutInflater.from(context).inflate(R.layout.receiver_layout_item,parent,false)
            return ReceiverViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val messageArrayList = messageArrayList.get(position)
        if(FirebaseAuth.getInstance().currentUser!!.uid.equals(messageArrayList.senderId)){
            return ITEM_SEND
        }

        else{
            return ITEM_RECEIVE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ITEM_SEND){
            (holder as SenderViewHolder).bind(messageArrayList.get(position), context!!)
        }

        else{
            (holder as ReceiverViewHolder).bind(messageArrayList.get(position), context!!)
        }
    }

    override fun getItemCount(): Int {
        return messageArrayList.size
    }

}