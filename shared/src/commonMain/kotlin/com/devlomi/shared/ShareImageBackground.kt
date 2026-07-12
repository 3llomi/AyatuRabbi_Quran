package com.devlomi.shared

import io.github.vinceglb.filekit.PlatformFile

expect object ShareImageBackground {
    fun addBackgroundColorToImage(
        imgFile: PlatformFile,
        colorHex: String,
        useWhiteColor: Boolean,
        file: PlatformFile
    ): String
}