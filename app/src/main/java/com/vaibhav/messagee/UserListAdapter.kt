package com.vaibhav.messagee

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.user_item_list.view.*

class UserListAdapter(var context: Context?, var userArrayList: ArrayList<Users>): RecyclerView.Adapter<UserListAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserListAdapter.ViewHolder, position: Int) {
        holder.itemView.apply {
            tvusername.text = userArrayList.get(position).name
            tvuserstatus.text = userArrayList.get(position).status
            Glide.with(context)
                .load(userArrayList.get(position).imageUri)
                .into(profile_image)

        }
    }

    override fun getItemCount(): Int {
        return userArrayList.size
    }
}