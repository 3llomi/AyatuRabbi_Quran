package com.devlomi.ayaturabbi.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.devlomi.ayaturabbi.util.isApi33OrAbove
import com.devlomi.shared.ui.App
import com.google.firebase.BuildConfig
import me.zhanghai.android.systemuihelper.SystemUiHelper
import java.io.File


class MainActivity : AppCompatActivity() {


    private lateinit var uiHelper: SystemUiHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uiHelper = SystemUiHelper(
            this,
            SystemUiHelper.LEVEL_IMMERSIVE,
            SystemUiHelper.FLAG_IMMERSIVE_STICKY
        )


        setContent {
            App(
                hideSystemUi = { hideSystemUi ->
                    if (hideSystemUi) {
                        uiHelper.hide()
                    } else {
                        uiHelper.show()
                    }
                },
                onShareText = { shareText(it) },
                onShareImage = { shareImage(it) }
            )
        }
        requestNotificationsPermissions()

    }

    private fun shareText(text: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(Intent.createChooser(shareIntent, null))
    }

    private fun shareImage(imagePath: String) {

        val uri = Uri.fromFile(File(imagePath))
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION


        val imageUri =
            FileProvider.getUriForFile(
                this,
                "${packageName}.provider",
                File(imagePath)
            )

        intent.putExtra(Intent.EXTRA_STREAM, imageUri)
        intent.type = "image/*"


        val chooser = Intent.createChooser(intent, "Share Using").apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val resInfoList: List<ResolveInfo> = this.packageManager
            .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            this.grantUriPermission(
                packageName,
                uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }

        startActivity(chooser)

    }

    private fun requestNotificationsPermissions() {
        if (isApi33OrAbove() && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
    }

}