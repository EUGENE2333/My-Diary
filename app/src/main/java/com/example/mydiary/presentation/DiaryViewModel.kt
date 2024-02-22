package com.example.mydiary.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydiary.data.LoginUiState
import com.example.mydiary.data.utils.PasswordManager
import com.example.mydiary.data.utils.PreferencesManager
import com.example.mydiary.data.utils.Utils
import com.example.mydiary.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    //val passwordManager: PasswordManager
    private val authRepository: AuthRepository,
   private val preferencesManager: PreferencesManager,
    ) : ViewModel(){

        @Inject
   lateinit var passwordManager: PasswordManager

     // login
     private val currentUser = authRepository.currentUser
    val hasUser: Boolean
        get() = authRepository.hasUser()

    var loginUiState by mutableStateOf(LoginUiState())
       private set


    val enabledFlow: Flow<Boolean> = preferencesManager.enabledFlow
    val isNoteFormat: Flow<Boolean> = preferencesManager.isFormatFlow




    private val _selectedColorTheme = MutableStateFlow(Color(0xFF1E6D65))
    var selectedColorTheme = _selectedColorTheme.asStateFlow()


    fun onUserNameChange(userName: String){
        loginUiState = loginUiState.copy(userName = userName)
    }
    fun onPasswordChange(password: String){
        loginUiState = loginUiState.copy(password = password)
    }
    fun onUserNameSignUpChange(userNameSignUp: String){
        loginUiState = loginUiState.copy(userNameSignUp = userNameSignUp)
    }
    fun onPasswordSignUpChange(passwordSignUp: String){
        loginUiState = loginUiState.copy(passwordSignUp = passwordSignUp)
    }
    fun onConfirmPasswordSignUpChange(confirmPasswordSignUp: String){
        loginUiState = loginUiState.copy(confirmPasswordSignUp = confirmPasswordSignUp)
    }

    private fun validateLoginForm() =
        loginUiState.userName.isNotBlank() &&
                loginUiState.password.isNotBlank()

    private fun validateSignUpForm() =
        loginUiState.userNameSignUp.isNotBlank() &&
                loginUiState.passwordSignUp.isNotBlank() &&
                loginUiState.confirmPasswordSignUp.isNotBlank()

    fun createUser(context: Context) = viewModelScope.launch {

        try {
            if(!validateSignUpForm()){
                throw IllegalArgumentException("email and password cannot be empty")
            }
            loginUiState = loginUiState.copy(isLoading = true)
            if(loginUiState.passwordSignUp != loginUiState.confirmPasswordSignUp){
                throw IllegalArgumentException(
                    "Password do not match"
                )
            }
            loginUiState = loginUiState.copy(signUpError = null)
            authRepository.createUser(loginUiState.userNameSignUp, loginUiState.passwordSignUp){
                    isSuccessful ->
                if(isSuccessful){
                   Toast.makeText(context, "success login", Toast.LENGTH_SHORT).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = true)
                    passwordManager.setUserAccount(currentUser?.email.toString())

                }else{
                    Toast.makeText(context, "login failed", Toast.LENGTH_SHORT).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = false)
                }
            }

        }catch(e:Exception){
               loginUiState = loginUiState.copy(signUpError = e.localizedMessage)
            e.printStackTrace()
        }finally {
            loginUiState = loginUiState.copy(isLoading = false)
        }
    }

    fun loginUser(context: Context) = viewModelScope.launch {
        try {
            if(!validateLoginForm()){
                throw IllegalArgumentException("email and password cannot be empty")
            }
            loginUiState = loginUiState.copy(isLoading = true)

            loginUiState = loginUiState.copy(loginError = null)
            authRepository.loginUser(loginUiState.userName, loginUiState.password){
                    isSuccessful ->
                if(isSuccessful){
                    Toast.makeText(context, "success login", Toast.LENGTH_SHORT).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = true)
                    passwordManager.setUserAccount(currentUser?.email.toString())

                }else{
                    Toast.makeText(context, "login failed", Toast.LENGTH_SHORT).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = false)
                }
            }
        }catch(e:Exception){
            loginUiState = loginUiState.copy(loginError = e.localizedMessage)
            e.printStackTrace()
        }finally {
            loginUiState = loginUiState.copy(isLoading = false)
        }
    }

    suspend fun setEnabled(enabled: Boolean) {
        preferencesManager.setEnabled(enabled)
    }

    suspend fun isNoteFormat(isNoteFormat: Boolean) {
        preferencesManager.isNoteFormat(isNoteFormat)
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