package com.build38.droidconlondon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    val okHttpClient = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        runNetworkCall()
    }

    private fun runNetworkCall() {
        val request = Request.Builder()
            .url("https://httpbin.org/get")
            .build()

        val textView: TextView = findViewById(R.id.center_text)

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }

                    val responseBodyString = response.body?.string() ?: "Response body is null"

                    runOnUiThread {
                        textView.text = responseBodyString
                    }
                }
            }
        })
    }
}