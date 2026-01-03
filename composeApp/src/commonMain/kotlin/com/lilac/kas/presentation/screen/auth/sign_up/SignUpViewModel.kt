package com.lilac.kas.presentation.screen.auth.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lilac.kas.domain.use_case.AuthUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val authUseCase: AuthUseCase
): ViewModel() {
    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    fun setEmail(email: String) {
        _state.value = _state.value.copy(email = email.trim().lowercase())
    }

    fun setUsername(username: String) {
        _state.value = _state.value.copy(username = username.trim().lowercase())
    }

    fun setFirstName(firstName: String) {
        _state.value = _state.value.copy(firstName = firstName)
    }

    fun setLastName(lastName: String) {
        _state.value = _state.value.copy(lastName = lastName)
    }

    fun setPassword(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    fun setConfirmPassword(confirmPassword: String) {
        _state.value = _state.value.copy(confirmPassword = confirmPassword)
    }

    fun togglePasswordVisibility() {
        _state.value = _state.value.copy(passwordVisible = !_state.value.passwordVisible)
    }

    fun toggleConfirmPasswordVisibility() {
        _state.value = _state.value.copy(confirmPasswordVisible = !_state.value.confirmPasswordVisible)
    }

    fun signUp(
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            authUseCase.register(
                email = _state.value.email,
                username = _state.value.username,
                password = _state.value.password,
                firstName = _state.value.firstName,
                lastName = _state.value.lastName
            )
                .onSuccess {
                    _state.value = _state.value.copy(isLoading = false)
                    onSuccess()
                }
                .onFailure {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = it.message ?: "Something went wrong"
                    )

                }
        }
    }

    fun signInWithGoogle() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                errorMessage = "Not yet implemented"
            )
            delay(2000L)
            _state.value = _state.value.copy(errorMessage = null)
        }
    }
}