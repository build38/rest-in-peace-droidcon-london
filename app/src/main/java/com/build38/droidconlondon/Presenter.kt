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
        // TODO: get url from constants or Build Config
        val request = Request.Builder()
            .url("https://httpbin.org/get")
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
}