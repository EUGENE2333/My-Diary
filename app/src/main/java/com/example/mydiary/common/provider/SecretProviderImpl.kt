package com.example.mydiary.common.provider

import android.content.Context
import androidx.annotation.StringRes
import com.example.mydiary.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecretProviderImpl @Inject constructor(@ApplicationContext private val context: Context) :
    SecretsProvider {

    override fun getSecretValue(id: SecretId): String {
        return when (id) {
            SecretId.REVENUECAT_API_KEY -> getSecret(R.string.revenuecat_sdk_key)
           // SecretId.GOOGLE_AUTH_SERVER_CLIENT_ID -> getSecret(R.string.android_client_id)

            else -> throw IllegalArgumentException("Invalid secret requested")
        }
    }

    private fun getSecret(@StringRes resId: Int) = context.getString(resId)
}