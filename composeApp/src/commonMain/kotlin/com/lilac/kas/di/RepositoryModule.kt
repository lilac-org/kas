package com.lilac.kas.di

import com.lilac.kas.data.repository.AuthRepositoryImpl
import com.lilac.kas.domain.repository.AuthRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
}