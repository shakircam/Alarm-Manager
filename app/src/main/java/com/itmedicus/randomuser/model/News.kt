package com.itmedicus.randomuser.model

data class News(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
){
    data class Article(

        val title: String,
        val excerpt: String,
        val imageUrl: String,
        val publishedAt: String,
        val source: String
    )
}