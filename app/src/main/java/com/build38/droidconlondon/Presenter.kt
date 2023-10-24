package com.build38.droidconlondon

import okhttp3.*
import java.io.IOException

internal class Presenter {

    val okHttpClient = OkHttpClient()
    private var view: ViewInterface? = null

    fun onViewAttached(view: ViewInterface) {
        this.view = view
    }

    fun onViewDetached() {
        this.view = null
    }

    fun onUserClickedRunNetworkCall(apiSecretsMode: ApiSecretsMode) {

        val secretToken = retrieveToken(apiSecretsMode)

        val request = Request.Builder()
            .url("${secretToken.first}/bearer")
            .addHeader("Authorization", "Bearer ${secretToken.second}")
            .build()

        okHttpClient.newCall(request).enqueue(handleResponse(apiSecretsMode))
    }

    private fun handleResponse(apiSecretsMode: ApiSecretsMode) = object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            view!!.showFailedNetworkCallWith(apiSecretsMode, e.message ?: "No info on error")
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (!response.isSuccessful) {
                    view!!.showFailedNetworkCallWith(apiSecretsMode, "Unexpected code $response")
                }

                val responseBodyString = response.body?.string() ?: "Response body is null"

                view!!.showSuccessfulNetworkCallWith(apiSecretsMode, responseBodyString)
            }
        }
    }

    private fun retrieveToken(apiSecretsMode: ApiSecretsMode): Pair<String, String> {
        return when (apiSecretsMode) {
            ApiSecretsMode.WITHOUT_CERTIFICATE_PINNING -> Pair(BASE_URL_NO_CERTIFICATE_PINNING, "{SecretToken_dr0idConL0Ndon}")
            ApiSecretsMode.HARDCODED_API_SECRETS -> Pair(BASE_URL_WITH_CERTIFICATE_PINNING, HardcodedSecretsProvider().retrieveApiSecrets())
            ApiSecretsMode.PROTECTED_API_SECRETS -> Pair(BASE_URL_WITH_CERTIFICATE_PINNING, ProtectedSecretsProvider().retrieveApiSecrets())
        }

    }

    companion object {
        private const val BASE_URL_NO_CERTIFICATE_PINNING = "https://httpbin.org"
        private const val BASE_URL_WITH_CERTIFICATE_PINNING = "https://httpbin.org" // TODO: is httpbin.dev.build38.cloud down?
    }
}