package com.devlomi.shared.ui

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.ui.window.ComposeUIViewController
import com.devlomi.shared.data.settings.SettingsRepository
import org.koin.compose.getKoin
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIImage

fun MainViewController() = ComposeUIViewController() {
    val settingsRepository = getKoin().get<SettingsRepository>()
    LaunchedEffect(Unit){
        settingsRepository.saveDeviceWidth(1260) //we use only 1260 on IOS
    }
    val vc = LocalUIViewController.current
    App(
        hideSystemUi = {
            //No Op on iOS, as system UI is handled differently
        },
        onShareText = {
            //share text using UIActivityViewController
            val ac = UIActivityViewController(activityItems = listOf(it), applicationActivities = null)
            vc.presentViewController(ac,true){}
        },
        onShareImage = {
            val uiImage = UIImage(contentsOfFile = it)
            val ac = UIActivityViewController(activityItems = listOf(uiImage), applicationActivities = null)
            vc.presentViewController(ac,true){}
        },
        onShareApp = {
            val ac = UIActivityViewController(activityItems = listOf(it), applicationActivities = null)
            vc.presentViewController(ac,true){}
        },
        exitApp = {
        // No Op on iOS, as apps are not exited programmatically
        }
    )
}