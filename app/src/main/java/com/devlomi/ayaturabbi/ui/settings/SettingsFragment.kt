package com.devlomi.ayaturabbi.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.devlomi.ayaturabbi.BuildConfig
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.settings_fragment.*

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.settings_fragment) {


    private val viewModel: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_version.text = BuildConfig.VERSION_NAME

        subscribeObservers()

        val mainViewModel =  ViewModelProvider(requireActivity()).get(MainViewModel::class.java)


        switch_disable_screen_lock.setOnCheckedChangeListener { compoundButton, b ->
            viewModel.onSwitchChange(b)
            mainViewModel.loadKeepScreenOn()
        }

        tv_share_app.setOnClickListener {


            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.type = "text/plain"

            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                resources.getString(R.string.share_app_text).format(getAppLink())
            )
            startActivity(sendIntent)
        }

        tv_website.setOnClickListener {
            launchWebsite("http://devlomi.com")
        }

        tv_follow_us.setOnClickListener {
            launchWebsite("https://twitter.com/3llomi")
        }

        tv_github.setOnClickListener {
            launchWebsite("https://github.com/3llomi/AyatuRabbi_Quran")
        }

        tv_rate_app.setOnClickListener {
            launchWebsite(getAppLink())
        }

        tv_our_apps.setOnClickListener {
            val devId = ""//TODO GET DEV ID
            launchWebsite("http://play.google.com/store/apps/dev?id=$devId")
        }


    }

    private fun getAppLink(): String {
        val appPackageName = requireContext().packageName
        return "https://play.google.com/store/apps/details?id=$appPackageName"
    }

    private fun subscribeObservers() {
        viewModel.preventScreenlock.observe(viewLifecycleOwner) { isEnabled ->
            switch_disable_screen_lock.isChecked = isEnabled
        }
    }

    private fun launchWebsite(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}