package com.sun.khobrakhobor


data class NewsResponse(
    val articles: List<Article>
)

data class Article(
    val title : String ?=null,
    val description : String?=null,
    val url : String?=null,
    val author : String?=null,
    val urlToImage : String?=null,
    val publishedAt : String?=null,
)


