package com.itmedicus.randomuser.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.model.BmiRecord

class BmiAdapter: RecyclerView.Adapter<BmiAdapter.BmiViewHolder> () {
    var list = mutableListOf<BmiRecord>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BmiViewHolder {
        return BmiViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.bmi_item, parent, false)
        )

    }

    override fun onBindViewHolder(holder: BmiViewHolder, position: Int) {
        val currentItem = list[position]

        holder.bmiScore.text = currentItem.bmiScore.toString()
        holder.bmiStatus.text = currentItem.bmiStatus
        holder.date.text = currentItem.date
        holder.height.text = currentItem.height
        holder.weight.text = currentItem.weight.toString()+"Kg"

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class BmiViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val bmiScore = itemView.findViewById(R.id.bmiText) as TextView
        val bmiStatus = itemView.findViewById(R.id.status) as TextView
        val date = itemView.findViewById(R.id.date) as TextView
        val height = itemView.findViewById(R.id.heightText) as TextView
        val weight = itemView.findViewById(R.id.weightText) as TextView


    }
    fun setData(list: MutableList<BmiRecord>){
        this.list = list
        notifyDataSetChanged()
    }

}