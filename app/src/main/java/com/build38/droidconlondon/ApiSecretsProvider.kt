package com.build38.droidconlondon

internal interface ApiSecretsProvider {
    fun retrieveApiSecrets(baseUrl: String): String
}
