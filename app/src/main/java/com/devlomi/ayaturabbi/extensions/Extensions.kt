package com.devlomi.ayaturabbi.extensions

import android.os.Bundle

//for some reason when using `bundle.getInt` it returns 0
//for this we've created this extension function to return null if key not exists
fun Bundle.getIntOrNull(key: String): Int? {
    if (this.containsKey(key)) {
        return getInt(key)
    }
    return null
}