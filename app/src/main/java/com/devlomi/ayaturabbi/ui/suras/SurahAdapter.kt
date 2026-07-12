package com.devlomi.ayaturabbi.ui.suras

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.databinding.ItemSurahBinding

class SurahAdapter(val listener: ((Surah) -> Unit)) :
    ListAdapter<Surah, SurahAdapter.SurahHolder>(Surah.diffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurahHolder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.item_surah, parent, false)
        return SurahHolder(row)
    }

    override fun onBindViewHolder(holder: SurahHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SurahHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemSurahBinding.bind(itemView)
        init {
            itemView.setOnClickListener {
                listener(getItem(adapterPosition))
            }
        }

        fun bind(surah: Surah) {
            binding.tvSurahName.text = surah.surahName
            val pos = surah.surahNumber
            binding.tvPageNumber.text = pos.toString()
        }
    }

}