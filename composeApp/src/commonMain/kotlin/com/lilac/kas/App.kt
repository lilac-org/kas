package com.lilac.kas

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lilac.kas.domain.service.AuthDataStoreManager
import com.lilac.kas.presentation.component.Center
import com.lilac.kas.presentation.navigation.Screen
import com.lilac.kas.presentation.navigation.rememberNavigator
import com.lilac.kas.presentation.screen.auth.sign_in.SignInScreen
import com.lilac.kas.presentation.screen.auth.sign_up.SignUpScreen
import com.lilac.kas.presentation.theme.KasTheme
import org.koin.compose.getKoin

@Composable
fun App() {
    KasTheme {
        val navController = rememberNavController()
        val navigator = rememberNavigator(navController)

        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxSize()
        ) {
            val auth = getKoin().get<AuthDataStoreManager>()

            NavHost(
                navController = navController,
                startDestination = if(auth.user == null) Screen.SignIn else Screen.Home
//                startDestination = Screen.SignIn
            ) {
                composable<Screen.SignIn> {
                    SignInScreen(
                        modifier = Modifier.fillMaxSize(),
                        onNavigateToSignUp = {
                            navigator.resetBackStackAndNavigateTo(Screen.SignUp)
                        },
                        onNavigateToHome = {
                            navigator.resetBackStackAndNavigateTo(Screen.Home)
                        }
                    )
                }

                composable<Screen.SignUp> {
                    SignUpScreen(
                        modifier = Modifier.fillMaxSize(),
                        onNavigateToSignIn = {
                            navigator.resetBackStackAndNavigateTo(Screen.SignIn)
                        },
                        onNavigateToHome = {
                            navigator.resetBackStackAndNavigateTo(Screen.Home)
                        }
                    )
                }

                composable<Screen.Home> {
                    Center(modifier = Modifier.fillMaxSize()) {
                        Text("Home Page")
                    }
                }
            }
        }
    }
}