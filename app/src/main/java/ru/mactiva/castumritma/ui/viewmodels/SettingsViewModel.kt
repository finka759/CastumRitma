package ru.mactiva.castumritma.ui.viewmodels

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel : ViewModel() {
    // Состояние темной темы
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme = _isDarkTheme.asStateFlow()

    fun toggleTheme(dark: Boolean) {
        _isDarkTheme.value = dark
    }

    fun changeLanguage(langCode: String) {
        // Логика смены языка через AppCompatDelegate (для Android 13+)
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(langCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}