package com.itmedicus.randomuser.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.model.Result


class HistoryAdapter(private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<HistoryAdapter.UserViewHolder> () {
    private var userList = mutableListOf<Result>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentItem = userList[position]

       // holder.nat.text = "Nationality: "+currentItem.nat
        holder.phone.text = "Phone: "+currentItem.phone
        holder.gender.text = "Gender: "+currentItem.gender
        holder.name.text = "Name: "+currentItem.name
        Glide.with(holder.image)
            .load(currentItem.picture)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            itemClickListener.onItemSend(position)
            val removedItem = userList.removeAt(position)
            userList.add(0, removedItem)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById(R.id.name) as TextView
//        val nat = itemView.findViewById(R.id.nationality) as TextView
        val phone = itemView.findViewById(R.id.phone) as TextView
        val gender = itemView.findViewById(R.id.gender) as TextView
        val image = itemView.findViewById(R.id.image) as ImageView
    }

    fun setData(userList: MutableList<Result>){
        this.userList = userList
        notifyDataSetChanged()
    }

}