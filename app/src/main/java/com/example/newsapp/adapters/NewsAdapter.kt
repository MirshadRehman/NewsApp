package com.example.newsapp.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.newsapp.R
import com.example.newsapp.data.model.Article
import com.example.newsapp.databinding.ItemNewsBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewsAdapter(private val listener: OnArticleClickListener) :
    ListAdapter<Article, NewsAdapter.ViewHolder>(DiffCallback()) {

    interface OnArticleClickListener {
        fun onArticleClicked(articleUrl: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
        holder.itemView.setOnClickListener {
            listener.onArticleClicked(article.url)
        }
    }

    class ViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            binding.tvTitle.text = article.title
            binding.tvDescription.text = article.description
            binding.tvSource.text = article.source.name
            binding.tvDateTime.text = formatDate(article.publishedAt)

            // Load image using Glide
            Glide.with(binding.root)
                .load(article.urlToImage)
                .placeholder(R.drawable.placeholder) // Set placeholder image
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.ivArticleImage)
        }

        private fun formatDate(date: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
            val parsedDate: Date = inputFormat.parse(date) ?: Date()
            return outputFormat.format(parsedDate)
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
}
