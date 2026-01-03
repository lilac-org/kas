package com.lilac.kas.di

import com.lilac.kas.domain.use_case.AuthUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::AuthUseCase)
}