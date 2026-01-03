package com.lilac.kas.di

import com.lilac.kas.presentation.screen.auth.sign_in.SignInViewModel
import com.lilac.kas.presentation.screen.auth.sign_up.SignUpViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SignInViewModel)
    viewModelOf(::SignUpViewModel)
}