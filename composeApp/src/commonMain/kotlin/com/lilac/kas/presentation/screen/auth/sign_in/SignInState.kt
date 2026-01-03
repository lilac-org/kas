package com.lilac.kas.presentation.screen.auth.sign_in

data class SignInState(
    val identifier: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val isFormValid: Boolean get() = identifier.isNotBlank() && password.isNotBlank()
}