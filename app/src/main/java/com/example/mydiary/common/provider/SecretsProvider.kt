package com.example.mydiary.common.provider

/**
 * An interface which can be injected in other modules to provide app secrets.
 */
interface SecretsProvider {

    fun getSecretValue(id: SecretId): String
}

enum class SecretId {
    REVENUECAT_API_KEY,
}