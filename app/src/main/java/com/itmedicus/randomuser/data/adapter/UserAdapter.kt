package com.itmedicus.randomuser.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.model.User

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder> () {
    private var userList = emptyList<User.Result>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.name.text = currentItem.name.toString()
        holder.phone.text = currentItem.phone
        holder.email.text = currentItem.email
        holder.gender.text = currentItem.gender

    }

    override fun getItemCount(): Int {
       return userList.size
    }

    class UserViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById(R.id.name) as TextView
        val phone = itemView.findViewById(R.id.phone) as TextView
        val email = itemView.findViewById(R.id.email) as TextView
        val gender = itemView.findViewById(R.id.gender) as TextView
    }
    fun setData(userList: List<User.Result>){
        this.userList = userList
        notifyDataSetChanged()
    }
}