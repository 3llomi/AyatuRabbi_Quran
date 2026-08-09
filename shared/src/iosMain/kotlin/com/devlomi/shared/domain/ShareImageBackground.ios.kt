package com.devlomi.shared.domain

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.path
import platform.CoreGraphics.CGBlendMode
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSURL
import platform.Foundation.NSFileManager
import platform.UIKit.UIColor
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetCurrentContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIRectFill
import platform.UIKit.UIScreen
import platform.Foundation.NSData
import platform.CoreGraphics.CGContextSetBlendMode
import platform.Foundation.writeToURL
import kotlinx.cinterop.useContents

actual object ShareImageBackground {
    actual fun addBackgroundColorToImage(
        imgFile: PlatformFile,
        colorHex: String,
        useWhiteColor: Boolean,
        file: PlatformFile
    ): String {
        val srcPath = imgFile.path
        val srcImage = UIImage.imageWithContentsOfFile(srcPath) ?: return ""

        val size = srcImage.size
        val width = size.useContents { width }
        val height = size.useContents { height }
        val scale = UIScreen.mainScreen.scale

        UIGraphicsBeginImageContextWithOptions(size, false, scale)

        // Fill background with provided color
        val bgColor = colorFromHex(colorHex) ?: UIColor.whiteColor()
        bgColor.setFill()
        val rect = CGRectMake(0.0, 0.0, width, height)
        UIRectFill(rect)

        // Draw original image on top
        srcImage.drawInRect(rect)

        // If requested, replace non-transparent pixels with white to improve contrast
        if (useWhiteColor) {
            val ctx = UIGraphicsGetCurrentContext()
            if (ctx != null) {
                CGContextSetBlendMode(ctx, CGBlendMode.kCGBlendModeSourceAtop)
                UIColor.whiteColor().setFill()
                UIRectFill(rect)
            }
        }

        val newImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()

        if (newImage == null) return ""

        val data: NSData = UIImagePNGRepresentation(newImage) ?: return ""

        val destPath = file.path
        // Ensure parent directory exists
        val parent = destPath.substringBeforeLast('/', "")
        if (parent.isNotEmpty()) {
            NSFileManager.defaultManager.createDirectoryAtPath(parent, true, null, null)
        }

        val url = NSURL.fileURLWithPath(destPath)
        val wrote = data.writeToURL(url, true)
        return if (wrote) destPath else ""
    }

    private fun colorFromHex(hex: String): UIColor? {
        var h = hex.replace("#", "")
        if (h.length == 3) {
            // expand shorthand like #fff
            h = "${h[0]}${h[0]}${h[1]}${h[1]}${h[2]}${h[2]}"
        }
        if (h.length == 6 || h.length == 8) {
            try {
                val hasAlpha = h.length == 8
                val a = if (hasAlpha) h.substring(0, 2).toInt(16) else 255
                val r = if (hasAlpha) h.substring(2, 4).toInt(16) else h.substring(0, 2).toInt(16)
                val g = if (hasAlpha) h.substring(if (hasAlpha) 4 else 2, if (hasAlpha) 6 else 4).toInt(16) else 0
                val b = if (hasAlpha) h.substring(if (hasAlpha) 6 else 4, if (hasAlpha) 8 else 6).toInt(16) else 0

                val af = a.toDouble() / 255.0
                val rf = r.toDouble() / 255.0
                val gf = g.toDouble() / 255.0
                val bf = b.toDouble() / 255.0

                return UIColor.colorWithRed(rf, green = gf, blue = bf, alpha = af)
            } catch (e: Throwable) {
                return null
            }
        }
        return null
    }
}