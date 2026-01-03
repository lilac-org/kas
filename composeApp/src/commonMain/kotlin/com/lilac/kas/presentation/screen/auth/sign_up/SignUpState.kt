package com.lilac.kas.presentation.screen.auth.sign_up

data class SignUpState(
    val email: String = "",
    val username: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val isFormValid: Boolean get() =
        email.isNotBlank()
                && username.isNotBlank()
                && firstName.isNotBlank()
                && lastName.isNotBlank()
                && password.isNotBlank()
                && confirmPassword.isNotBlank()
                && password == confirmPassword
}
