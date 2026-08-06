package com.devlomi.shared.ui.settings

sealed class SettingsNavigationEvents {
    data object ShareApp : SettingsNavigationEvents()
}
