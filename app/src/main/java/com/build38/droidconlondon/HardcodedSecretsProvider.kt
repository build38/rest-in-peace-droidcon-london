package com.build38.droidconlondon

internal class HardcodedSecretsProvider: ApiSecretsProvider {
    override fun retrieveApiSecrets(baseUrl: String): String {
        return if (baseUrl.endsWith("httpbin.dmuth.org")) {
            "HardcodedToken_dr0idConL0Ndon_1"
        } else if (baseUrl.endsWith("httpbin.org") ){
            "HardcodedToken_dr0idConL0Ndon_2"
        } else {
            throw UnsupportedOperationException("Unknown Base URL $baseUrl")
        }
    }
}
