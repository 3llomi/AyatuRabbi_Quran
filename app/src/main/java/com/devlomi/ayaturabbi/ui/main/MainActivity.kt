package com.devlomi.ayaturabbi.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.extensions.deviceWidthPixels
import com.devlomi.ayaturabbi.extensions.navigateSafely
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
//        val findNavController = Navigation.findNavController(this, R.id.fragment_container)

        viewModel.saveDeviceWidth(deviceWidthPixels())

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment

        val navController = navHostFragment.navController


        if (viewModel.hasDownloadedFiles()) {
            navController.navigateSafely(R.id.quranPage, R.id.action_mainFragment_to_quranPage)

        } else {
            navController.navigateSafely(
                R.id.downloadFragment,
                R.id.action_mainFragment_to_downloadFragment
            )

        }





    }



}