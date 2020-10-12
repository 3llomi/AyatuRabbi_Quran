package com.devlomi.ayaturabbi.ui.main

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.devlomi.ayaturabbi.network.DownloadRepository
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.db.ayahinfo.AyahInfoDao
import com.devlomi.ayaturabbi.db.quran_ar.QuranDBDao
import com.devlomi.ayaturabbi.extensions.deviceWidthPixels
import com.devlomi.ayaturabbi.extensions.navigateSafely
import com.devlomi.ayaturabbi.network.DownloadService
import com.devlomi.ayaturabbi.network.DownloadingResource
import com.devlomi.ayaturabbi.util.IntentConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

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