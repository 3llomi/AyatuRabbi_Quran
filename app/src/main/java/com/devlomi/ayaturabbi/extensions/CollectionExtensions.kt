package com.devlomi.ayaturabbi.extensions

 fun <T> MutableList<T>.removed(item: T): MutableList<T> {
    val newList = this.toMutableList()
    newList.remove(item)
    return newList
}
