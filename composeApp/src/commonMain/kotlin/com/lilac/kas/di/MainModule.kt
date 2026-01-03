package com.lilac.kas.di

import com.lilac.kas.data.repository.AuthRepositoryImpl
import com.lilac.kas.data.service.AuthDataStoreManagerImpl
import com.lilac.kas.data.service.HttpClientFactory
import com.lilac.kas.domain.repository.AuthRepository
import com.lilac.kas.domain.service.AuthDataStoreManager
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val nonWebHttpClientEngine = CIO.create()

val mainModule = module {
    singleOf(::Settings)
    singleOf(::AuthDataStoreManagerImpl).bind<AuthDataStoreManager>()
    single<HttpClient> { HttpClientFactory.create(get(), get()) }
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
}

