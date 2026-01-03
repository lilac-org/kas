package com.lilac.kas.presentation.screen.auth.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lilac.kas.domain.use_case.AuthUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignInViewModel(
    private val authUseCase: AuthUseCase
): ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun setIdentifier(identifier: String) {
        _state.value = _state.value.copy(identifier = identifier.lowercase())
    }

    fun setPassword(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    fun togglePasswordVisibility() {
        _state.value = _state.value.copy(passwordVisible = !_state.value.passwordVisible)
    }

    fun signIn(
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            authUseCase.login(
                identifier = _state.value.identifier,
                password = _state.value.password
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