package com.devlomi.ayaturabbi.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.databinding.MainActivityBinding
import com.devlomi.ayaturabbi.extensions.deviceWidthPixels
import com.devlomi.ayaturabbi.util.isApi33OrAbove
import dagger.hilt.android.AndroidEntryPoint
import me.zhanghai.android.systemuihelper.SystemUiHelper


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private val viewModel: MainViewModel by viewModels()

    private lateinit var uiHelper: SystemUiHelper

    private var currentDestination = -1

    private lateinit var binding: MainActivityBinding

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        //hide system bars if the user presses the recent button or minimized the aoo
        if (currentDestination == R.id.quranPage) {
            uiHelper.hide()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uiHelper = SystemUiHelper(
            this,
            SystemUiHelper.LEVEL_IMMERSIVE,
            SystemUiHelper.FLAG_IMMERSIVE_STICKY
        )

        subscribeObservers()

        viewModel.loadKeepScreenOn()
        viewModel.saveDeviceWidth(deviceWidthPixels())

        requestNotificationsPermissions()

        ViewCompat.setOnApplyWindowInsetsListener(binding.fragmentRoot) { view, insets ->
            if (currentDestination == R.id.quranPage) {
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