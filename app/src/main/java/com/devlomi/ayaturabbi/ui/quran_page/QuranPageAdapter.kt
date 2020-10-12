package com.devlomi.ayaturabbi.ui.quran_page

import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devlomi.ayaturabbi.R
import kotlinx.android.synthetic.main.item_quran_page.view.*

class QuranPageAdapter : RecyclerView.Adapter<QuranPageAdapter.QuranPageHolder>() {

    private var paths = listOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuranPageHolder {
        val row =
            LayoutInflater.from(parent.context).inflate(R.layout.item_quran_page, parent, false)

        return QuranPageHolder(row)

    }

    override fun onBindViewHolder(holder: QuranPageHolder, position: Int) {
        val nightModeTextBrightness = 255f
        val imgPath = paths[position]
        Glide.with(holder.itemView.context).load(imgPath).into(holder.itemView.img_quran)
        val matrix = floatArrayOf(
            -1f,
            0f,
            0f,
            0f,
            nightModeTextBrightness,
            0f,
            -1f,
            0f,
            0f,
            nightModeTextBrightness,
            0f,
            0f,
            -1f,
            0f,
            nightModeTextBrightness,
            0f,
            0f,
            0f,
            1f,
            0f
        )
        holder.itemView.img_quran.colorFilter = ColorMatrixColorFilter(matrix)
    }

    override fun getItemCount() = paths.size

    fun setItems(paths: List<String>) {
        this.paths = paths
        notifyDataSetChanged()
    }


    inner class QuranPageHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}