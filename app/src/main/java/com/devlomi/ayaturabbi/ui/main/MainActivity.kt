package com.devlomi.ayaturabbi.ui.main

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.devlomi.ayaturabbi.BuildConfig
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.extensions.deviceWidthPixels
import com.devlomi.ayaturabbi.extensions.navigateSafely
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_activity.*
import me.zhanghai.android.systemuihelper.SystemUiHelper
import java.io.File


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private val viewModel: MainViewModel by viewModels()

    private lateinit var uiHelper: SystemUiHelper

    private var currentDestination = -1

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        //hide system bars if the user presses the recent button or minimized the aoo
        if (currentDestination == R.id.quranPage) {
            uiHelper.hide()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        uiHelper = SystemUiHelper(
            this,
            SystemUiHelper.LEVEL_IMMERSIVE,
            SystemUiHelper.FLAG_IMMERSIVE_STICKY
        )

        subscribeObservers()

        viewModel.loadKeepScreenOn()
        viewModel.saveDeviceWidth(deviceWidthPixels())


        ViewCompat.setOnApplyWindowInsetsListener(frament_root) { view, insets ->
            if (currentDestination == R.id.quranPage || currentDestination == R.id.shareTypeBottomSheet) {
                view.updatePadding(bottom = 0, top = 0)
            } else {
                view.updatePadding(
                    bottom = insets.systemWindowInsetBottom,
                    top = insets.systemWindowInsetTop
                )
            }
            insets
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment

        val navController = navHostFragment.navController

        setupNavController(navHostFragment, navController)


        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            currentDestination = destination.id
            if (destination.id == R.id.quranPage) {
                uiHelper.hide()
            } else {
                uiHelper.show()
            }
        }


    }


    private fun setupNavController(
        navHostFragment: NavHostFragment,
        navController: NavController
    ) {
        val graphInflater = navHostFragment.navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.nav_graph)

        val destination = if (viewModel.hasDownloadedFiles()) {
            R.id.quranPage
        } else {
            R.id.downloadFragment
        }

        navGraph.startDestination = destination
        navController.graph = navGraph
    }

    private fun subscribeObservers() {
        viewModel.keepScreenOn.observe(this) { keepScreenOn ->
            setScreenOnFlags(keepScreenOn)
        }

        viewModel.hideUI.observe(this) {
            uiHelper.hide()
        }


    }

    private fun setScreenOnFlags(keepScreenOn: Boolean) {
        if (keepScreenOn)
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        else
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }


}