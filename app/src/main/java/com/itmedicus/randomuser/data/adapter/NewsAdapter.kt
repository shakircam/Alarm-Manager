package com.itmedicus.randomuser.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.model.News

class NewsAdapter(private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder> () {
    private var newsList = emptyList<News.Article>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false))
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = newsList[position]

        holder.title.text = currentItem.title
        holder.excerpt.text = currentItem.excerpt
        holder.published.text = currentItem.publishedAt
        holder.source.text = currentItem.source

        Glide.with(holder.image)
            .load(currentItem.imageUrl)
            .into(holder.image)
        holder.itemView.setOnClickListener {
            itemClickListener.onItemSend(position)
        }

    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    class NewsViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

        val title = itemView.findViewById(R.id.tvTitle) as TextView
        val excerpt = itemView.findViewById(R.id.tvDescription) as TextView
        val image = itemView.findViewById(R.id.ivArticleImage) as ImageView
        val published = itemView.findViewById(R.id.tvPublishedAt) as TextView
        val source = itemView.findViewById(R.id.tvSource) as TextView

    }

    fun setData(newsList: List<News.Article>){
        this.newsList = newsList
        notifyDataSetChanged()
    }

}