package com.vaibhav.messagee.Adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vaibhav.messagee.ModelClass.MessageModel

class MessageListAdapter(var context: Context?, var messageArrayList: ArrayList<MessageModel>) : RecyclerView.Adapter<MessageListAdapter.ViewHolder>() {
    inner class senderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    inner class receiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return messageArrayList.size
    }
}