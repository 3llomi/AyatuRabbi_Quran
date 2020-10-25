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
import com.devlomi.ayaturabbi.util.WhiteColorFilter
import kotlinx.android.synthetic.main.item_quran_page.view.*

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

        init {
            itemView.setOnClickListener {
                adapterListener?.onClick(adapterPosition, getItem(adapterPosition))
            }
        }

        fun bind(quranPageItem: QuranPageItem) {
            val imgPath = quranPageItem.imageFilePath
            Glide.with(itemView.context).load(imgPath).into(itemView.img_quran)

            pageScale?.observe(lifecycleOwner) { scale ->
                if (itemView.img_quran.scaleX != scale) {
                    itemView.img_quran.scaleX = scale
                    itemView.img_quran.scaleY = scale
                }
            }
            useWhiteColorLiveData.observe(lifecycleOwner, { useWhiteTextColor ->
                var tvTextColor = if (useWhiteTextColor) ContextCompat.getColor(
                    itemView.context,
                    R.color.white
                ) else
                    ContextCompat.getColor(itemView.context, R.color.black)

                itemView.tv_juzoa_name.setTextColor(tvTextColor)
                itemView.tv_surah_name.setTextColor(tvTextColor)
                itemView.tv_page_number.setTextColor(tvTextColor)

                if (useWhiteTextColor) {

                    setColorFilterForText(itemView.img_quran)
                } else {
                    itemView.img_quran.clearColorFilter()
                }
            })



            itemView.tv_juzoa_name.text = String.format(
                itemView.context.resources.getString(
                    R.string.aljuzoa,
                    quranPageItem.juzoaNumberText
                )
            )

            itemView.tv_surah_name.text = String.format(
                itemView.context.resources.getString(
                    R.string.surah,
                    quranPageItem.surahName
                )
            )

            itemView.tv_page_number.text = quranPageItem.pageNumberLocalized
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