package com.devlomi.ayaturabbi.ui.quran_page

import android.graphics.ColorMatrixColorFilter
import android.view.*
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.databinding.ItemQuranPageBinding
import com.devlomi.shared.QuranPageItem
import com.devlomi.shared.WhiteColorFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class QuranPageAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val useWhiteColorState: StateFlow<Boolean>
) :
    ListAdapter<QuranPageItem, QuranPageAdapter.QuranPageHolder>(diffCallback) {

    companion object {
        val diffCallback = object :
            DiffUtil.ItemCallback<QuranPageItem>() {
            override fun areItemsTheSame(oldItem: QuranPageItem, newItem: QuranPageItem): Boolean {
                return oldItem.pageNumber == newItem.pageNumber
            }

            override fun areContentsTheSame(
                oldItem: QuranPageItem,
                newItem: QuranPageItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    var adapterListener: AdapterListener? = null
    var pageScale: StateFlow<Float>? = null

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


            lifecycleOwner.lifecycleScope.launch {

                lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        pageScale?.collectLatest { scale ->
                            if (binding.imgQuran.scaleX != scale) {
                                binding.imgQuran.scaleX = scale
                                binding.imgQuran.scaleY = scale
                            }
                        }
                    }
                    launch {

                    useWhiteColorState.collectLatest { useWhiteTextColor ->
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
                }
                }

            }



            binding.tvJuzoaName.text = String.format(
                itemView.context.resources.getString(
                    R.string.aljuzoa,
                    quranPageItem.juzoaNumberText,
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