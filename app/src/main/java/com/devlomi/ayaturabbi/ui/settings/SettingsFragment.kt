package com.devlomi.ayaturabbi.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.devlomi.ayaturabbi.BuildConfig
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.databinding.SettingsFragmentBinding
import com.devlomi.shared.MainViewModel
import com.devlomi.shared.SettingsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment(R.layout.settings_fragment) {


    private val viewModel: SettingsViewModel by viewModel()

    private var _binding: SettingsFragmentBinding? = null
    private val binding: SettingsFragmentBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvVersion.text = BuildConfig.VERSION_NAME

        subscribeObservers()

        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)


        binding.switchDisableScreenLock.setOnCheckedChangeListener { compoundButton, b ->
            viewModel.onSwitchChange(b)
            mainViewModel.loadKeepScreenOn()
        }

        binding.tvShareApp.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.type = "text/plain"

            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                resources.getString(R.string.share_app_text).format(getAppLink())
            )
            startActivity(sendIntent)
        }

        binding.tvWebsite.setOnClickListener {
            launchWebsite("http://devlomi.com")
        }

        binding.tvFollowUs.setOnClickListener {
            launchWebsite("https://twitter.com/3llomi")
        }

        binding.tvGithub.setOnClickListener {
            launchWebsite("https://github.com/3llomi/AyatuRabbi_Quran")
        }

        binding.tvRateApp.setOnClickListener {
            launchWebsite(getAppLink())
        }

//        tv_our_apps.setOnClickListener {
//            launchWebsite("https://play.google.com/store/apps/developer?id=AbdulAlim+Rajjoub+%28Devlomi%29")
//        }


    }

    private fun getAppLink(): String {
        val appPackageName = requireContext().packageName
        return "https://play.google.com/store/apps/details?id=$appPackageName"
    }

    private fun subscribeObservers() {
        lifecycleScope.launch {
            viewModel.preventScreenlock.flowWithLifecycle(lifecycle).collectLatest {
                binding.switchDisableScreenLock.isChecked = it
            }
        }

    }

    private fun launchWebsite(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}