package com.devlomi.ayaturabbi

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.color_picker_layout.view.*

class ColorPickerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = -1
) : FrameLayout(context, attrs, defStyle), View.OnClickListener {
    var colorPickerListener: ColorPickerListener? = null

    init {
        val view = View.inflate(context, R.layout.color_picker_layout, null)
        addView(view)
        val dkgray = view.findViewById<View>(R.id.color_dkgray)
        val beige = view.findViewById<View>(R.id.color_beige)
        val white = view.findViewById<View>(R.id.color_white)
        val dkblue = view.findViewById<View>(R.id.color_dkblue)

        dkgray.setOnClickListener(this)
        beige.setOnClickListener(this)
        white.setOnClickListener(this)
        dkblue.setOnClickListener(this)

    }

    override fun onClick(p0: View) {
        val pickedColor = when (p0.id) {
            R.id.color_dkgray -> ColorItem.DKGRAY
            R.id.color_beige -> ColorItem.BEIGE
            R.id.color_white -> ColorItem.WHITE
            else -> ColorItem.DKBLUE
        }

        colorPickerListener?.onItemClick(pickedColor)
    }
}

interface ColorPickerListener {
    fun onItemClick(colorItem: ColorItem)
}

enum class ColorItem {
    DKGRAY, BEIGE, WHITE, DKBLUE;


    companion object {
        fun fromName(colorItemName: String):ColorItem{
            return  when(colorItemName){
                DKGRAY.name -> DKGRAY
                BEIGE.name -> BEIGE
                WHITE.name -> WHITE
                else -> DKBLUE
            }
        }
    }
}