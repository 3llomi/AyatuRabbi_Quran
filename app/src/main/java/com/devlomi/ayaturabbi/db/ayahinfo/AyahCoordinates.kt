package com.devlomi.ayaturabbi.db.ayahinfo

data class AyahCoordinates(
    val page: Int,
    val ayahCoordinates: Map<String, MutableList<AyahInfo>?>


){
    companion object{
         const val THRESHOLD_PERCENTAGE = 0.015f
    }
}
