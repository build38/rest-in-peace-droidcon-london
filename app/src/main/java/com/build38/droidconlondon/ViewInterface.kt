package com.build38.droidconlondon

internal interface ViewInterface {

    fun showSuccessfulNetworkCallWith(secretsMode: ApiSecretsMode, body: String)
    fun showFailedNetworkCallWith(secretsMode: ApiSecretsMode, error: String)
}