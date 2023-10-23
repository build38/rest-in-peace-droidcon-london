package com.build38.droidconlondon

internal class ProtectedSecretsProvider: ApiSecretsProviderInterface {
    override fun retrieveApiSecrets(): String {
        return decryptSecretToken()
    }

    private fun decryptSecretToken(): String {
        val key = getEncryptionKey()
        val encryptedToken = "TODO" // TODO: get from build setting or last bytes from image
        val token = "TODO" // TODO: decrypt
        return token
    }

    private fun getEncryptionKey(): ByteArray {
        return ByteArray(0)
    }
}