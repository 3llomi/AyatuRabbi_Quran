package com.devlomi.ayaturabbi.ui.bookmarks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.databinding.ItemBookmarkBinding
import com.devlomi.ayaturabbi.db.bookmark.Bookmark

class BookmarkAdapter :
    ListAdapter<Bookmark, BookmarkAdapter.BookmarkHolder>(Bookmark.diffCallBack) {

    var adapterListener: AdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkHolder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.item_bookmark, parent, false)
        return BookmarkHolder(row)
    }

    override fun onBindViewHolder(holder: BookmarkHolder, position: Int) {
        holder.bind(getItem(holder.adapterPosition))
    }



    inner class BookmarkHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemBookmarkBinding.bind(itemView)
        init {
            itemView.setOnClickListener {
                adapterListener?.onItemClick(adapterPosition, getItem(adapterPosition))
            }

            binding.btnDeleteBookmark.setOnClickListener {
                adapterListener?.onDeleteClick(adapterPosition, getItem(adapterPosition))
            }
        }

        fun bind(bookmark: Bookmark) {
            binding.tvDate.text = bookmark.formattedTimestamp
            binding.tvNote.text = bookmark.note
            binding.tvSurahName.text = bookmark.surahName
            binding.tvPageNumber.text = bookmark.pageNumber.toString()
        }
    }
}

interface AdapterListener {
    fun onItemClick(position: Int, bookmark: Bookmark)
    fun onDeleteClick(position: Int, bookmark: Bookmark)
}