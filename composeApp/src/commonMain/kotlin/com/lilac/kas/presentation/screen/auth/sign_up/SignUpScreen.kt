package com.lilac.kas.presentation.screen.auth.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lilac.kas.presentation.component.NotImplementedScreen
import com.lilac.kas.presentation.component.calculateWindowSize
import com.lilac.kas.presentation.screen.auth.component.AuthDivider
import com.lilac.kas.presentation.screen.auth.component.AuthNavigateButton
import com.lilac.kas.presentation.screen.auth.component.AuthSubtitle
import com.lilac.kas.presentation.screen.auth.component.AuthTextField
import com.lilac.kas.presentation.screen.auth.component.AuthTitle
import com.lilac.kas.presentation.screen.auth.component.GoogleSignInButton
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onNavigateToSignIn: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SignUpViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val windowSizeClass = calculateWindowSize()
    val isCompactWidth = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LaunchedEffect(state.errorMessage) {
            if(state.errorMessage != null) {
                snackbarHostState
                    .showSnackbar(
                        message = state.errorMessage!!
                    )
            }
        }

        if(!isCompactWidth) {
            NotImplementedScreen()
            return@Scaffold
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            AuthTitle(
                first = "Create ",
                second = "Account",
                modifier = Modifier
                    .align(Alignment.Start)
            )

            Spacer(modifier = Modifier.size(12.dp))

            AuthSubtitle(
                text = "Let's get started",
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.size(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AuthTextField(
                    value = state.firstName,
                    label = "First Name",
                    onValueChange = viewModel::setFirstName,
                    modifier = Modifier.weight(1f),
                    leadingIcon = null
                )

                AuthTextField(
                    value = state.lastName,
                    label = "Last Name",
                    onValueChange = viewModel::setLastName,
                    modifier = Modifier.weight(1f),
                    leadingIcon = null
                )
            }

            Spacer(modifier = Modifier.size(4.dp))

            AuthTextField(
                value = state.username,
                label = "Username",
                onValueChange = viewModel::setUsername,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = Icons.Filled.Edit
            )

            Spacer(modifier = Modifier.size(4.dp))

            AuthTextField(
                value = state.email,
                label = "Email",
                onValueChange = viewModel::setEmail,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = Icons.Filled.Email
            )

            Spacer(modifier = Modifier.size(4.dp))

            AuthTextField(
                value = state.password,
                hidden = !state.passwordVisible,
                label = "Password",
                onValueChange = viewModel::setPassword,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = Icons.Filled.Password,
                trailingContent = {
                    IconButton(
                        onClick = viewModel::togglePasswordVisibility,
                    ) {
                        Icon(
                            imageVector = if (!state.passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.size(4.dp))

            AuthTextField(
                value = state.confirmPassword,
                hidden = !state.confirmPasswordVisible,
                label = "Confirm Password",
                onValueChange = viewModel::setConfirmPassword,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = Icons.Filled.Password,
                trailingContent = {
                    IconButton(
                        onClick = viewModel::toggleConfirmPasswordVisibility,
                    ) {
                        Icon(
                            imageVector = if (!state.confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.size(16.dp))

            Button(
                onClick = {
                    viewModel.signUp {
                        onNavigateToHome()
                    }
                },
                enabled = state.isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(OutlinedTextFieldDefaults.MinHeight),
                shape = MaterialTheme.shapes.medium,
            ) {
                Text(
                    text = "Register",
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.size(32.dp))

            AuthDivider()

            Spacer(modifier = Modifier.size(16.dp))

            GoogleSignInButton {
                viewModel.signInWithGoogle()
            }

            Spacer(modifier = Modifier.weight(1f))

            AuthNavigateButton(
                support = "Already have an account? ",
                main = "Sign In",
                onNavigate = onNavigateToSignIn
            )
        }
    }
}