package com.sun.khobrakhobor.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sun.khobrakhobor.Article
import com.sun.khobrakhobor.R

class NewsAdapter(private val articles: List<Article>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_news, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(article: Article) {
            itemView.apply {
                findViewById<TextView>(R.id.mainheading).text = article.title
                findViewById<TextView>(R.id.content).text = article.description
                findViewById<TextView>(R.id.author).text = article.author
                findViewById<TextView>(R.id.time).text = "Published At: ${article.publishedAt}"
                if(article.urlToImage!=null){
                    Glide.with(context).load(article.urlToImage).into(findViewById<ImageView>(R.id.imageview))
                }
                else {
                    Glide.with(context).load(R.drawable.fornoimage)
                        .into(findViewById<ImageView>(R.id.imageview))
                }
                setOnClickListener {
                    // Handle item click
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                    context.startActivity(intent)
                }
            }
        }
    }
}