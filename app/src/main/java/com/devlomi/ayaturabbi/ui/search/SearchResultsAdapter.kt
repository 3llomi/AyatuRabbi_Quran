package com.devlomi.ayaturabbi.ui.search

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.databinding.ItemSearchBinding
import com.devlomi.shared.SearchResult

class SearchResultsAdapter(val listener: (SearchResult) -> Unit) :
    ListAdapter<SearchResult, SearchResultsAdapter.SearchResultHolder>(
        AsyncDifferConfig.Builder(
            diffCallback
        ).build()
    ) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<SearchResult>() {
            override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
                return oldItem.ayahNumber == newItem.ayahNumber && oldItem.pageNumber == newItem.pageNumber && oldItem.highlightedText == newItem.highlightedText
            }

            override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
                return oldItem.ayahNumber == newItem.ayahNumber
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultHolder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return SearchResultHolder(row)
    }

    override fun onBindViewHolder(holder: SearchResultHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class SearchResultHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemSearchBinding.bind(itemView)
        init {
            itemView.setOnClickListener {
                listener(getItem(adapterPosition))
            }
        }

        fun bind(searchResult: SearchResult) {
            val query = searchResult.highlightedText
            val spannableString = SpannableString(searchResult.foundText).apply {
                val length = query.length
                val indexOf = indexOf(query)
                setSpan(
                    ForegroundColorSpan(Color.RED),
                    indexOf,
                    indexOf + length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            binding.tvAyah.text = spannableString


            binding.tvSurahName.text = searchResult.surahName
            binding.tvAyahNumber.text = searchResult.ayahNumber.toString()
            binding.tvPageNumber.text = searchResult.pageNumber.toString()


        }
    }

}