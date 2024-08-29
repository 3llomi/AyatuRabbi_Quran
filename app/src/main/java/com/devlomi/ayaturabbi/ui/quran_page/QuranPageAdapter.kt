package com.devlomi.ayaturabbi.ui.quran_page

import android.graphics.ColorMatrixColorFilter
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.databinding.ItemQuranPageBinding
import com.devlomi.ayaturabbi.util.WhiteColorFilter

class QuranPageAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val useWhiteColorLiveData: LiveData<Boolean>
) :
    ListAdapter<QuranPageItem, QuranPageAdapter.QuranPageHolder>(QuranPageItem.diffCallback) {

    var adapterListener: AdapterListener? = null
    var pageScale: LiveData<Float>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuranPageHolder {
        val row =
            LayoutInflater.from(parent.context).inflate(R.layout.item_quran_page, parent, false)

        return QuranPageHolder(row)

    }


    override fun onBindViewHolder(holder: QuranPageHolder, position: Int) {
        val quranPageItem = getItem(holder.adapterPosition)
        holder.bind(quranPageItem)
    }


    inner class QuranPageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemQuranPageBinding.bind(itemView)

        init {
            itemView.setOnClickListener {
                adapterListener?.onClick(adapterPosition, getItem(adapterPosition))
            }
        }

        fun bind(quranPageItem: QuranPageItem) {
            val imgPath = quranPageItem.imageFilePath
            Glide.with(itemView.context).load(imgPath).into(binding.imgQuran)

            pageScale?.observe(lifecycleOwner) { scale ->
                if (binding.imgQuran.scaleX != scale) {
                    binding.imgQuran.scaleX = scale
                    binding.imgQuran.scaleY = scale
                }
            }
            useWhiteColorLiveData.observe(lifecycleOwner) { useWhiteTextColor ->
                var tvTextColor = if (useWhiteTextColor) ContextCompat.getColor(
                    itemView.context,
                    R.color.white
                ) else
                    ContextCompat.getColor(itemView.context, R.color.black)

                binding.tvJuzoaName.setTextColor(tvTextColor)
                binding.tvSurahName.setTextColor(tvTextColor)
                binding.tvPageNumber.setTextColor(tvTextColor)

                if (useWhiteTextColor) {

                    setColorFilterForText(binding.imgQuran)
                } else {
                    binding.imgQuran.clearColorFilter()
                }
            }



            binding.tvJuzoaName.text = String.format(
                itemView.context.resources.getString(
                    R.string.aljuzoa,
                    quranPageItem.juzoaNumberText
                )
            )

            binding.tvSurahName.text = String.format(
                itemView.context.resources.getString(
                    R.string.surah,
                    quranPageItem.surahName
                )
            )

            binding.tvPageNumber.text = quranPageItem.pageNumberLocalized
        }

        private fun setColorFilterForText(
            imgView: ImageView
        ) {
            imgView.colorFilter = ColorMatrixColorFilter(WhiteColorFilter.matrix)
        }

    }

}

interface AdapterListener {
    fun onClick(pos: Int, quranPageItem: QuranPageItem)
//    fun onLongClick(pos: Int, quranPageItem: QuranPageItem, motionEvent: MotionEvent)
}