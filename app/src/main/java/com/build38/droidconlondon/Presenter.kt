package com.build38.droidconlondon

import okhttp3.*
import java.io.IOException

internal class Presenter {

    private val okHttpClient = OkHttpClient()
    private var view: ViewInterface? = null

    fun onViewAttached(view: ViewInterface) {
        this.view = view
    }

    fun onViewDetached() {
        this.view = null
    }

    fun onUserClickedRunNetworkCall(apiSecretsMode: ApiSecretsMode) {
        val baseUrl = getBaseUrl(apiSecretsMode)
        val apiToken = getApiSecretsProvider(apiSecretsMode).retrieveApiSecrets(baseUrl)

        val request = Request.Builder()
            .url("${baseUrl}/get")
            .addHeader("Authorization", "Bearer $apiToken")
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

    private fun getBaseUrl(apiSecretsMode: ApiSecretsMode): String {
        return when (apiSecretsMode) {
            ApiSecretsMode.WITHOUT_CERTIFICATE_PINNING -> BASE_URL_NO_CERTIFICATE_PINNING
            else -> BASE_URL_WITH_CERTIFICATE_PINNING
        }
    }

    private fun getApiSecretsProvider(apiSecretsMode: ApiSecretsMode): ApiSecretsProvider {
        return when (apiSecretsMode) {
            ApiSecretsMode.PROTECTED_API_SECRETS -> EncryptedSecretsProvider()
            else -> HardcodedSecretsProvider()
        }
    }

    companion object {
        private const val BASE_URL_NO_CERTIFICATE_PINNING = "https://httpbin.dmuth.org"
        private const val BASE_URL_WITH_CERTIFICATE_PINNING = "https://httpbin.org"
    }
}
