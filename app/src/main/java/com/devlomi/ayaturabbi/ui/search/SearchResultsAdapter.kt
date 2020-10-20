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
import kotlinx.android.synthetic.main.item_search.view.*

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
//
//class SearchResultsAdapter(val listener: (SearchResult) -> Unit) :
//    RecyclerView.Adapter<SearchResultsAdapter.SearchResultHolder>() {
//
//    private var list = mutableListOf<SearchResult>()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultHolder {
//        val row = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
//        return SearchResultHolder(row)
//    }
//
////    fun submitList(list: MutableList<SearchResult>) {
////        this.list = list
////        notifyDataSetChanged()
////    }
//
//    override fun onBindViewHolder(holder: SearchResultHolder, position: Int) {
//
//        holder.bind(list[position])
//    }

    inner class SearchResultHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

            itemView.tv_ayah.text = spannableString


            itemView.tv_surah_name.text = searchResult.surahName
            itemView.tv_ayah_number.text = searchResult.ayahNumber.toString()
            itemView.tv_page_number.text = searchResult.pageNumber.toString()


        }
    }

}