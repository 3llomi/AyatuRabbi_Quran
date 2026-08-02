package com.devlomi.shared.domain

enum class ColorItem {
    DKGRAY, BEIGE, WHITE, DKBLUE;


    companion object {
        fun fromName(colorItemName: String): ColorItem {
            return  when(colorItemName){
                DKGRAY.name -> DKGRAY
                BEIGE.name -> BEIGE
                WHITE.name -> WHITE
                else -> DKBLUE
            }
        }
    }
}