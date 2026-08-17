package com.devlomi.shared.ui.settings

sealed class SettingsEvents {
    data object OnShareAppClick : SettingsEvents()
    object OnBackClick : SettingsEvents()
    data class OnSwitchChange(val isEnabled: Boolean) : SettingsEvents()
}