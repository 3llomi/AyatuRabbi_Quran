package com.devlomi.ayaturabbi.db.ayahinfo

import android.graphics.RectF
import android.util.SparseArray
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

class AyahInfoRepository @Inject constructor(
    private val ayahInfoDao: AyahInfoDao,
) {
    private val pageWidth = 1260 //TODO MOVE THIS TO REAL VARIABLE

    suspend fun getVersesBoundsForPage(page: Int): AyahCoordinates {
        val versesBounds = ayahInfoDao.getVersesBoundsForPage(page)

        val ayahBounds = mutableMapOf<String, MutableList<AyahInfo>?>()

        versesBounds.forEach { ayahInfo ->

            val sura = ayahInfo.sura_number
            val ayah = ayahInfo.ayah_number
            val key = "$sura:$ayah"
            var bounds: MutableList<AyahInfo>? = ayahBounds[key]
            if (bounds == null) {
                bounds = ArrayList<AyahInfo>()
            }
            var last: AyahInfo? = null
            if (bounds!!.size > 0) {
                last = bounds[bounds.size - 1]
            }

            if (last != null && last.line_number === ayahInfo.line_number) {
                last.engulf(ayahInfo.getBounds())
            } else {
                bounds.add(ayahInfo)
            }
            ayahBounds[key] = bounds
        }


        return AyahCoordinates(page, ayahBounds)
    }


    private fun normalizePageAyahs(ayahCoordinates: AyahCoordinates): AyahCoordinates? {
        val original = ayahCoordinates.ayahCoordinates

        val normalizedMap =
            mutableMapOf<String, MutableList<AyahInfo>?>()

        val keys = original.keys
        for (key in keys) {
            val normalBounds: List<AyahInfo>? = original[key]
            if (normalBounds != null) {
                normalizedMap[key] = normalizeAyahBounds(normalBounds)?.toMutableList()
            }
        }
        return AyahCoordinates(ayahCoordinates.page, normalizedMap)
    }

    private fun normalizeAyahBounds(ayahBounds: List<AyahInfo>): List<AyahInfo>? {
        val total = ayahBounds.size
        return if (total < 2) {
            ayahBounds
        } else if (total < 3) {
            consolidate(ayahBounds[0], ayahBounds[1])
        } else {
            val middle: AyahInfo = ayahBounds[1]
            for (i in 2 until total - 1) {
                middle.engulf(ayahBounds[i].getBounds())
            }
            var top: List<AyahInfo> = consolidate(ayahBounds[0], middle)
            val topSize = top.size
            // the first parameter is essentially middle (after its consolidation with the top line)
            val bottom: List<AyahInfo> = consolidate(
                top[topSize - 1],
                ayahBounds[total - 1]
            )
            val result: MutableList<AyahInfo> = ArrayList<AyahInfo>()
            if (topSize == 1) {
                bottom
            } else if (topSize + bottom.size > 4) {
                // this happens when a verse spans 3 incomplete lines (i.e. starts towards the end of
                // one line, takes one or more whole lines, and ends early on in the line). in this case,
                // just remove the duplicates.

                // add the first parts of top
                for (i in 0 until topSize - 1) {
                    result.add(top[i])
                }

                // resolve the middle part which may overlap with bottom
                val lastTop: AyahInfo = top[topSize - 1]
                val firstBottom: AyahInfo = bottom[0]
                if (lastTop.equals(firstBottom)) {
                    // only add one if they're both the same
                    result.add(lastTop)
                } else {
                    // if one contains the other, add the larger one
                    val topRect: RectF = lastTop.getBounds()
                    val bottomRect: RectF = firstBottom.getBounds()
                    if (topRect.contains(bottomRect)) {
                        result.add(lastTop)
                    } else if (bottomRect.contains(topRect)) {
                        result.add(firstBottom)
                    } else {
                        // otherwise add both
                        result.add(lastTop)
                        result.add(firstBottom)
                    }
                }

                // add everything except the first bottom entry
                var i = 1
                val size = bottom.size
                while (i < size) {
                    result.add(bottom[i])
                    i++
                }
                result
            } else {
                // re-consolidate top and middle again, since middle may have changed
                top = consolidate(top[0], bottom[0])
                result.addAll(top)
                if (bottom.size > 1) {
                    result.add(bottom[1])
                }
                result
            }
        }
    }

    private fun consolidate(top: AyahInfo, bottom: AyahInfo): List<AyahInfo> {
        var top: AyahInfo = top
        var bottom: AyahInfo = bottom
        val firstRect: RectF = top.getBounds()
        val lastRect: RectF = bottom.getBounds()
        var middle: AyahInfo? = null

        // only 2 lines - let's see if any of them are full lines
        val threshold =
            (AyahCoordinates.THRESHOLD_PERCENTAGE * pageWidth) as Int
        val firstIsFullLine = abs(firstRect.right - lastRect.right) < threshold
        val secondIsFullLine = abs(firstRect.left - lastRect.left) < threshold
        if (firstIsFullLine && secondIsFullLine) {
            top.engulf(bottom.getBounds())
            return listOf<AyahInfo>(top)
        } else if (firstIsFullLine) {
            lastRect.top = firstRect.bottom
            val bestStartOfLine = Math.max(firstRect.right, lastRect.right)
            firstRect.right = bestStartOfLine
            lastRect.right = bestStartOfLine
            top = top.withBounds(firstRect)
            bottom = bottom.withBounds(lastRect)
        } else if (secondIsFullLine) {
            firstRect.bottom = lastRect.top
            val bestEndOfLine = Math.min(firstRect.left, lastRect.left)
            firstRect.left = bestEndOfLine
            lastRect.left = bestEndOfLine
            top = top.withBounds(firstRect)
            bottom = bottom.withBounds(lastRect)
        } else {
            // neither one is a full line, let's generate a middle entry to join them if they have
            // anything in common (i.e. any part of them intersects)
            if (lastRect.left < firstRect.right) {
                val middleBounds = RectF(
                    lastRect.left,  /* top= */
                    firstRect.bottom,
                    Math.min(firstRect.right, lastRect.right),  /* bottom= */
                    lastRect.top
                )
//                middle = AyahInfo(top.line_number, top.getPosition(), middleBounds)
                middle = top.copy().withBounds(middleBounds)
            }
        }
        val result: MutableList<AyahInfo> = ArrayList<AyahInfo>()
        result.add(top)
        if (middle != null) {
            result.add(middle)
        }
        result.add(bottom)
        return result
    }




}