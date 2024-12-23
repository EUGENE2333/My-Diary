package com.example.mydiary.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.ViewModel
import com.example.mydiary.data.utils.PasswordManager
import com.example.mydiary.data.utils.PreferencesManager
import com.example.mydiary.data.utils.Utils
import com.example.mydiary.domain.repository.AuthRepository
import com.example.mydiary.presentation.compose.mainComposables.subscription.revenuecat.RevenueCatController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    val passwordManager: PasswordManager,
    private val authRepository: AuthRepository,
   private val preferencesManager: PreferencesManager,
    private val revenueCatController: RevenueCatController

    ) : ViewModel() {

    // login
    private val currentUser = authRepository.currentUser
    val hasUser: Boolean
        get() = authRepository.hasUser()


    val enabledFlow: Flow<Boolean> = preferencesManager.enabledFlow
    val isNoteFormat: Flow<Boolean> = preferencesManager.isFormatFlow


    private val _selectedColorTheme = MutableStateFlow(Color(0xFF1E6D65))
    var selectedColorTheme = _selectedColorTheme.asStateFlow()

    suspend fun setEnabled(enabled: Boolean) {
        preferencesManager.setEnabled(enabled)
    }

    suspend fun isNoteFormat(isNoteFormat: Boolean) {
        preferencesManager.isNoteFormat(isNoteFormat)
    }

    suspend fun hasActiveSubscription(): Boolean {
        return revenueCatController.getSubscriberInfo()?.hasActiveSubscription == true
    }

    val colorThemes = Utils.colorThemes

    fun setSelectedColorTheme(color: Color) {
        _selectedColorTheme.value = color
        passwordManager.setColorTheme(color) // Save the selected color theme using PasswordManager
    }

    val fonts = Utils.fonts

    private val _selectedFont = MutableStateFlow(fonts[0])
    var selectedFont = _selectedFont.asStateFlow()

    fun setSelectedFontTheme(font: FontFamily) {
        _selectedFont.value = font
        passwordManager.setFontTheme(font) // Save the selected font theme using PasswordManager
    }
}