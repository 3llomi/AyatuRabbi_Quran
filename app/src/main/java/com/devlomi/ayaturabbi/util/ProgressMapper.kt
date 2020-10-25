package com.devlomi.ayaturabbi.util


object ProgressMapper {
    fun mapToView(value: Float): Int {
        return when (value) {
            1.0f -> 0
            1.01f -> 10
            1.02f -> 20
            1.03f -> 30
            1.04f -> 40
            1.05f -> 50
            1.06f -> 60
            1.07f -> 70
            1.08f -> 80
            1.09f -> 90
            1.1f -> 100
            else -> 0
        }
    }

    fun mapToScale(position: Int): Float {
        return if (position == 0) 1.0f else {
            val i = position / 100f
            val toFloat = (i + 1.0).toFloat()
            toFloat
        }
    }

}

