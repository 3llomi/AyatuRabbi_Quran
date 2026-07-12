package com.devlomi.shared

import android.graphics.*
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.parent
import io.github.vinceglb.filekit.path
import java.io.File
import java.io.FileOutputStream
import androidx.core.graphics.toColorInt


actual object ShareImageBackground {

    actual fun addBackgroundColorToImage(
        imgFile: PlatformFile,
        colorHex: String,
        useWhiteColor: Boolean,
        file: PlatformFile
    ): String {
        val imgBitmap = BitmapFactory.decodeFile(imgFile.path)

        val newBitmap =
            Bitmap.createBitmap(imgBitmap.width, imgBitmap.height, Bitmap.Config.ARGB_8888)


        val canvas = Canvas(newBitmap)
        canvas.drawColor(colorHex.toColorInt())
        canvas.drawBitmap(imgBitmap, 0f, 0f, getPaint(useWhiteColor))


        val finalFile = File(file.parent()?.path!!, file.name)

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