package com.devlomi.ayaturabbi.db.ayahinfo

import android.graphics.Matrix
import android.graphics.RectF
import android.util.SparseArray
import android.widget.ImageView
import java.util.*

object ImageAyahUtils {
    private fun getAyahFromKey(key: String): SuraAyah? {
        val parts = key.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var result: SuraAyah? = null
        if (parts.size == 2) {
            try {
                val sura = parts[0].toInt()
                val ayah = parts[1].toInt()
                result = SuraAyah(sura, ayah)
            } catch (e: Exception) {
                // no op
            }
        }
        return result
    }

    fun getAyahFromCoordinates(
        coords: Map<String, List<AyahInfo>?>?,
        imageView: ImageView?, xc: Float, yc: Float
    ): SuraAyah? {
        if (coords == null || imageView == null) {
            return null
        }
        val pageXY = getPageXY(xc, yc, imageView) ?: return null
        val x = pageXY[0]
        val y = pageXY[1]
        var closestLine = -1
        var closestDelta = -1
        val lineAyahs = SparseArray<MutableList<String>>()
        val keys = coords.keys
        for (key in keys) {
            val bounds: List<AyahInfo> = coords[key] ?: continue
            for (b in bounds) {
                // only one AyahBound will exist for an ayah on a particular line
                val line: Int = b.line_number
                var items = lineAyahs[line]
                if (items == null) {
                    items = ArrayList()
                }
                items.add(key)
                lineAyahs.put(line, items)
                val boundsRect: RectF = b.getBounds()
                if (boundsRect.contains(x, y)) {
                    return getAyahFromKey(key)
                }
                val delta = Math.min(
                    Math.abs(boundsRect.bottom - y).toInt(),
                    Math.abs(boundsRect.top - y).toInt()
                )
                if (closestDelta == -1 || delta < closestDelta) {
                    closestLine = b.line_number
                    closestDelta = delta
                }
            }
        }
        if (closestLine > -1) {
            var leastDeltaX = -1
            var closestAyah: String? = null
            val ayat: List<String>? = lineAyahs[closestLine]
            if (ayat != null) {
                for (ayah in ayat) {
                    val bounds: List<AyahInfo> = coords[ayah] ?: continue
                    for (b in bounds) {
                        if (b.line_number > closestLine) {
                            // this is the last ayah in ayat list
                            break
                        }
                        val boundsRect: RectF = b.getBounds()
                        if (b.line_number === closestLine) {
                            // if x is within the x of this ayah, that's our answer
                            if (boundsRect.right >= x && boundsRect.left <= x) {
                                return getAyahFromKey(ayah)
                            }

                            // otherwise, keep track of the least delta and return it
                            val delta = Math.min(
                                Math.abs(boundsRect.right - x).toInt(),
                                Math.abs(boundsRect.left - x).toInt()
                            )
                            if (leastDeltaX == -1 || delta < leastDeltaX) {
                                closestAyah = ayah
                                leastDeltaX = delta
                            }
                        }
                    }
                }
            }
            if (closestAyah != null) {
                return getAyahFromKey(closestAyah)
            }
        }
        return null
    }



    private fun getPageXY(
        screenX: Float, screenY: Float, imageView: ImageView?
    ): FloatArray? {
        if (imageView!!.drawable == null) {
            return null
        }
        var results: FloatArray? = null
        val inverse = Matrix()
        if (imageView.imageMatrix.invert(inverse)) {
            results = FloatArray(2)
            inverse.mapPoints(results, floatArrayOf(screenX, screenY))
            results[1] = results[1] - imageView.paddingTop
        }
        return results
    }

    fun getYBoundsForHighlight(
        coordinateData: Map<String?, List<AyahInfo>?>, sura: Int, ayah: Int
    ): RectF? {
        val ayahBounds: List<AyahInfo> = coordinateData["$sura:$ayah"]
            ?: return null
        var ayahBoundsRect: RectF? = null
        for (bounds in ayahBounds) {
            if (ayahBoundsRect == null) {
                ayahBoundsRect = bounds.getBounds()
            } else {
                ayahBoundsRect.union(bounds.getBounds())
            }
        }
        return ayahBoundsRect
    }
}