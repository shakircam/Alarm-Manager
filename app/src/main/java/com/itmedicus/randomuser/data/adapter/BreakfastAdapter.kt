package com.itmedicus.randomuser.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.model.Breakfast

class BreakfastAdapter(private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<BreakfastAdapter.BreakfastViewHolder> () {

    var list = mutableListOf<Breakfast>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreakfastViewHolder {
        return BreakfastViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_food_menu, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BreakfastViewHolder, position: Int) {
        val currentItem = list[position]

        holder.title.text = currentItem.title
        holder.kcal.text = currentItem.kcal.toString()+"kcal"
        holder.fat.text = currentItem.fat.toString()+"g"
        holder.protin.text = currentItem.protein.toString()+"g"
        holder.carbs.text = currentItem.carbs.toString()+"g"
        holder.card.setBackgroundResource(R.drawable.item_nutr)
        holder.saveBt.setOnClickListener {

        }

        holder.itemView.setOnClickListener {
            itemClickListener.onItemSend(position)
        }

        Glide.with(holder.image)
            .load(currentItem.image)
            .into(holder.image)

    }

    override fun getItemCount(): Int {
       return list.size
    }


    class BreakfastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image = itemView.findViewById(R.id.image) as ImageView
        val title = itemView.findViewById(R.id.title) as TextView
        val kcal = itemView.findViewById(R.id.kcal) as TextView
        val saveBt = itemView.findViewById(R.id.image1) as ImageView
        val fat = itemView.findViewById(R.id.fatText) as TextView
        val protin = itemView.findViewById(R.id.protinText) as TextView
        val carbs = itemView.findViewById(R.id.carbsText) as TextView
        val card = itemView.findViewById(R.id.materialcard) as MaterialCardView

    }

    fun setData(list: MutableList<Breakfast>){
        this.list = list
        notifyDataSetChanged()
    }

}