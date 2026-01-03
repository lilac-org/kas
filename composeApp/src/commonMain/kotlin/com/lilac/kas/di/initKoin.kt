package com.lilac.kas.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun initKoin(
    config: (KoinApplication.() -> Unit)? = null
) {
    startKoin {
        config?.invoke(this)
        modules(
            platformModule,
            mainModule,
            serviceModule,
            repositoryModule,
            useCaseModule,
            viewModelModule
        )
    }
}