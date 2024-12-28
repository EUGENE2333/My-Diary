package com.example.mydiary.presentation.onboarding

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydiary.data.utils.PasswordManager
import com.example.mydiary.data.utils.PreferencesManager
import com.example.mydiary.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginOrSignUpViewModel @Inject constructor(
    val passwordManager: PasswordManager,
    private val authRepository: AuthRepository,
    private val preferencesManager: PreferencesManager,
): ViewModel(){

    private val currentUser = authRepository.currentUser
    val isNoteFormat: Flow<Boolean> = preferencesManager.isFormatFlow

    var loginUiState by mutableStateOf(LoginUiState())
        private set

    fun onUserNameChange(userName: String) {
        loginUiState = loginUiState.copy(userName = userName)
    }

    fun onPasswordChange(password: String) {
        loginUiState = loginUiState.copy(password = password)
    }

    fun onUserNameSignUpChange(userNameSignUp: String) {
        loginUiState = loginUiState.copy(userNameSignUp = userNameSignUp)
    }

    fun onPasswordSignUpChange(passwordSignUp: String) {
        loginUiState = loginUiState.copy(passwordSignUp = passwordSignUp)
    }

    fun onConfirmPasswordSignUpChange(confirmPasswordSignUp: String) {
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
            if (!validateSignUpForm()) {
                throw IllegalArgumentException("email and password cannot be empty")
            }
            loginUiState = loginUiState.copy(isLoading = true)
            if (loginUiState.passwordSignUp != loginUiState.confirmPasswordSignUp) {
                throw IllegalArgumentException(
                    "Password do not match"
                )
            }
            loginUiState = loginUiState.copy(signUpError = null)
            authRepository.createUser(
                loginUiState.userNameSignUp,
                loginUiState.passwordSignUp
            ) { isSuccessful ->
                if (isSuccessful) {
                    Toast.makeText(context, "success login", Toast.LENGTH_SHORT).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = true)
                    passwordManager.setUserAccount(currentUser?.email.toString())
                  //  loginOrSignupInteractor.identifyUser(currentUser.uid.toString())


                } else {
                    Toast.makeText(context, "login failed", Toast.LENGTH_SHORT).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = false)
                }
            }

        } catch (e: Exception) {
            loginUiState = loginUiState.copy(signUpError = e.localizedMessage)
            e.printStackTrace()
        } finally {
            loginUiState = loginUiState.copy(isLoading = false)
        }
    }

    fun loginUser(context: Context) = viewModelScope.launch {
        try {
            if (!validateLoginForm()) {
                throw IllegalArgumentException("email and password cannot be empty")
            }
            loginUiState = loginUiState.copy(isLoading = true)

            loginUiState = loginUiState.copy(loginError = null)
            authRepository.loginUser(loginUiState.userName, loginUiState.password) { isSuccessful ->
                if (isSuccessful) {
                    Toast.makeText(context, "success login", Toast.LENGTH_SHORT).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = true)
                    passwordManager.setUserAccount(currentUser?.email.toString())

                } else {
                    Toast.makeText(context, "login failed", Toast.LENGTH_SHORT).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = false)
                }
            }
        } catch (e: Exception) {
            loginUiState = loginUiState.copy(loginError = e.localizedMessage)
            e.printStackTrace()
        } finally {
            loginUiState = loginUiState.copy(isLoading = false)
        }
    }
}