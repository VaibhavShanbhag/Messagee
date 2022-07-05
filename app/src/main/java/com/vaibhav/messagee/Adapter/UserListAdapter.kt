package com.vaibhav.messagee.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vaibhav.messagee.Activity.ChatActivity
import com.vaibhav.messagee.ModelClass.Users
import com.vaibhav.messagee.R
import kotlinx.android.synthetic.main.user_item_list.view.*

class UserListAdapter(var context: Context?, var userArrayList: ArrayList<Users>): RecyclerView.Adapter<UserListAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            tvusername.text = userArrayList.get(position).name
            tvuserstatus.text = userArrayList.get(position).status
            Glide.with(context)
                .load(userArrayList.get(position).imageUri)
                .into(recevier_profile_image)
            lluserlist.setOnClickListener {
                Intent(context, ChatActivity::class.java).also {
                    it.putExtra("name",userArrayList.get(position).name)
                    it.putExtra("receiverImage",userArrayList.get(position).imageUri)
                    it.putExtra("uid",userArrayList.get(position).uid)
                    context.startActivity(it)
                    (context as Activity).finish()
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return userArrayList.size
    }
}