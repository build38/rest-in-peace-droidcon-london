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
            .url("${BASE_URL}/bearer")
            .addHeader("Authorization", "Bearer $secretToken")
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

    private fun retrieveToken(apiSecretsMode: ApiSecretsMode): String {
        return when (apiSecretsMode) {
            ApiSecretsMode.NO_API_SECRETS -> "{SecretToken_dr0idConL0Ndon}"
            ApiSecretsMode.HARDCODED_API_SECRETS -> HardcodedSecretsProvider().retrieveApiSecrets()
            ApiSecretsMode.PROTECTED_API_SECRETS -> ProtectedSecretsProvider().retrieveApiSecrets()
        }

    }

    companion object {
        private const val BASE_URL = "https://httpbin.org"
    }
}