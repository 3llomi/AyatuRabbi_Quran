package com.devlomi.ayaturabbi.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.databinding.MainActivityBinding
import com.devlomi.ayaturabbi.extensions.deviceWidthPixels
import com.devlomi.ayaturabbi.util.isApi33OrAbove
import com.devlomi.shared.ui.main.MainViewModel
import com.devlomi.shared.ui.App
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.zhanghai.android.systemuihelper.SystemUiHelper
import org.koin.androidx.viewmodel.ext.android.viewModel


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
            App { hideSystemUi ->
                if (hideSystemUi) {
                    uiHelper.hide()
                } else {
                    uiHelper.show()
                }
            }
        }
        requestNotificationsPermissions()

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