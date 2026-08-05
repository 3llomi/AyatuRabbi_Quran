package com.devlomi.shared.domain

object ProgressMapper {
    fun mapToView(value: Float): Int {
        return when (value) {
            1.0f -> 0
            1.01f -> 1
            1.02f -> 2
            1.03f -> 3
            1.04f -> 4
            1.05f -> 5
            1.06f -> 6
            1.07f -> 7
            1.08f -> 8
            1.09f -> 90
            1.1f -> 10
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