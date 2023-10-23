package com.build38.droidconlondon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class MainActivity : AppCompatActivity(), ViewInterface {

    private var presenter = Presenter()
    private lateinit var textViewOnUi: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewOnUi = findViewById<TextView>(R.id.center_text)

        presenter.onViewAttached(this)

        presenter.onUserClickedRunNetworkCall(ApiSecretsMode.NO_API_SECRETS)
    }

    override fun showSuccessfulNetworkCallWith(secretsMode: ApiSecretsMode, body: String) {
        runOnUiThread {
            textViewOnUi.text = "Secret mode: $secretsMode and body is: \n $body"
        }
    }

    override fun showFailedNetworkCallWith(secretsMode: ApiSecretsMode, error: String) {
        Log.d(LOG_TAG, "Network call failed on $secretsMode mode with the error: $error")
    }

    override fun onDestroy() {
        presenter.onViewDetached()
        super.onDestroy()
    }

    companion object {
        const val LOG_TAG = "DroidCon London App"
    }
}