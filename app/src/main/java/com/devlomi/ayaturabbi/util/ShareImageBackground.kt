package com.devlomi.ayaturabbi.util

import android.graphics.*
import java.io.File
import java.io.FileOutputStream


object ShareImageBackground {

    fun addBackgroundColorToImage(
        imgFilePath: String,
        backgroundColor: Int,
        useWhiteColor: Boolean,
        file: File
    ): String {
        val imgBitmap = BitmapFactory.decodeFile(imgFilePath)

        val newBitmap =
            Bitmap.createBitmap(imgBitmap.width, imgBitmap.height, Bitmap.Config.ARGB_8888)


        val canvas = Canvas(newBitmap)
        canvas.drawColor(backgroundColor)
        canvas.drawBitmap(imgBitmap, 0f, 0f, getPaint(useWhiteColor))


        val finalFile = File(file.parent!!, file.name)

        finalFile.parentFile?.mkdirs()
        finalFile.createNewFile()
        val outputStream =
            FileOutputStream(finalFile)
        newBitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
        outputStream.close()
        outputStream.flush()

        return finalFile.path

    }

    private fun getPaint(useWhiteColor: Boolean): Paint? {
        if (useWhiteColor) {
            val colorMatrix = ColorMatrix(WhiteColorFilter.matrix)
            val colorFilter = ColorMatrixColorFilter(colorMatrix)
            return Paint().apply { this.colorFilter = colorFilter }
        }
        return null
    }


}