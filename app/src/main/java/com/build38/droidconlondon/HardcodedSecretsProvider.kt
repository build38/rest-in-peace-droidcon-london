package com.build38.droidconlondon

internal class HardcodedSecretsProvider: ApiSecretsProviderInterface {
    override fun retrieveApiSecrets(): String {
        // TODO: move to Build Config and retrieve from there
        return "AAAAAAAA"
    }
}