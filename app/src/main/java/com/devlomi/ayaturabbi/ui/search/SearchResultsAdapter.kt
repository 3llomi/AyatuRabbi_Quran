package com.devlomi.ayaturabbi.ui.search

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.databinding.ItemSearchBinding

class SearchResultsAdapter(val listener: (SearchResult) -> Unit) :
    ListAdapter<SearchResult, SearchResultsAdapter.SearchResultHolder>(
        AsyncDifferConfig.Builder(
            SearchResult.diffCallback
        ).build()
    ) {


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