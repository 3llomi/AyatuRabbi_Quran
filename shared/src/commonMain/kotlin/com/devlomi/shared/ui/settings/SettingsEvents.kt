package com.devlomi.shared.ui.settings

sealed class SettingsEvents {
    data class OnSwitchChange(val isEnabled: Boolean) : SettingsEvents()
}