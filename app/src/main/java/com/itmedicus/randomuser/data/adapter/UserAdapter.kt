package com.itmedicus.randomuser.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.json.Dami
import com.itmedicus.randomuser.model.Result
import com.itmedicus.randomuser.model.User
import com.itmedicus.randomuser.ui.fragment.ListFragmentDirections

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder> () {
    private var userList = emptyList<Dami.Results>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentItem = userList[position]

       // holder.name.text = currentItem.cell.toString()
        holder.nat.text = "Nationality: "+currentItem.nat
       // holder.phone.text = currentItem.results[position].phone.toString()
        holder.email.text = "Email: "+currentItem.email
        holder.gender.text = "Gender: "+currentItem.gender
        holder.itemView.setOnClickListener {
            val nat = currentItem.nat
            val email= currentItem.email
            val gender= currentItem.gender
            val result= Result(gender, email, nat)
            val action = ListFragmentDirections.actionListFragmentToDetailsFragment(result)
            it?.findNavController()?.navigate(action)
        }

    }

    override fun getItemCount(): Int {
       return userList.size
    }

    class UserViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById(R.id.name) as TextView
        val nat = itemView.findViewById(R.id.phone) as TextView
        val email = itemView.findViewById(R.id.email) as TextView
        val gender = itemView.findViewById(R.id.gender) as TextView
    }
    fun setData(userList: List<Dami.Results>){
        this.userList = userList
        notifyDataSetChanged()
    }
}