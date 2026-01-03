package com.lilac.kas.presentation.screen.auth.sign_in

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
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
import com.lilac.kas.presentation.screen.auth.component.*
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onNavigateToSignUp: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SignInViewModel = koinViewModel()
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
            if (state.errorMessage != null) {
                snackbarHostState
                    .showSnackbar(
                        message = state.errorMessage!!
                    )
            }
        }

        if (!isCompactWidth) {
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
                first = "Welcome ",
                second = "Back",
                modifier = Modifier
                    .align(Alignment.Start)
            )

            Spacer(modifier = Modifier.size(12.dp))

            AuthSubtitle(
                text = "Let's continue your journey",
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.size(12.dp))

            AuthTextField(
                value = state.identifier,
                label = "Email/Username",
                onValueChange = viewModel::setIdentifier,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = Icons.Filled.Person,
            )

            Spacer(modifier = Modifier.size(4.dp))

            AuthTextField(
                value = state.password,
                label = "Password",
                hidden = !state.passwordVisible,
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

            TextButton(
                onClick = {},
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "Forgot Password?",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            Button(
                onClick = {
                    viewModel.signIn {
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
                    text = "Continue",
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.size(32.dp))

            AuthDivider()

            Spacer(modifier = Modifier.size(16.dp))

            GoogleSignInButton {
                viewModel.signInWithGoogle()
            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            AuthNavigateButton(
                support = "New here? ",
                main = "Create an account",
                onNavigate = onNavigateToSignUp
            )
        }
    }
}