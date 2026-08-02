package com.devlomi.shared.common

//fun <T> MutableList<T>.removed(item: T): MutableList<T> {
//    val newList = this.toMutableList()
//    newList.remove(item)
//    return newList
//}
fun <T> List<T>.removed(item: T): List<T> {
    val newList = this.toMutableList()
    newList.remove(item)
    return newList
}
