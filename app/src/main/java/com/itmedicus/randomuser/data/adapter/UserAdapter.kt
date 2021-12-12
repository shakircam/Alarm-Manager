package com.itmedicus.randomuser.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.model.Dami
import com.itmedicus.randomuser.model.Result
import com.itmedicus.randomuser.ui.fragment.ListFragmentDirections

class UserAdapter(private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<UserAdapter.UserViewHolder> () {
    private var userList = emptyList<Dami.Results>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentItem = userList[position]

        holder.nat.text = "Nationality: "+currentItem.nat
        holder.phone.text = "Phone: "+currentItem.phone
        holder.gender.text = "Gender: "+currentItem.gender
        holder.name.text = "Name: "+currentItem.name.first+ " "+currentItem.name.last


        Glide.with(holder.image)
            .load(currentItem.picture.medium)
            .into(holder.image)
        //for fragment
       /* holder.itemView.setOnClickListener {
            val nat = currentItem.nat
            val email= currentItem.email
            val gender= currentItem.gender
            val result= Result(gender, email, nat)
            val action = ListFragmentDirections.actionListFragmentToDetailsFragment(result)
            it?.findNavController()?.navigate(action)
        }*/

        //for activity
        holder.itemView.setOnClickListener {
            itemClickListener.onItemSend(position)
        }

    }

    override fun getItemCount(): Int {
       return userList.size
    }

    class UserViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById(R.id.name) as TextView
        val nat = itemView.findViewById(R.id.nationality) as TextView
        val phone = itemView.findViewById(R.id.phone) as TextView
        val gender = itemView.findViewById(R.id.gender) as TextView
        val image = itemView.findViewById(R.id.image) as ImageView
    }
    fun setData(userList: List<Dami.Results>){
        this.userList = userList
        notifyDataSetChanged()
    }
}