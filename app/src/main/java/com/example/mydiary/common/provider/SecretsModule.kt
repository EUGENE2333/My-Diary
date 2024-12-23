package com.example.mydiary.common.provider

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface SecretsModule {

    @Singleton
    @Binds
    fun bindsScanMathRepository(
        secretsProvider: SecretProviderImpl
    ): SecretsProvider

}