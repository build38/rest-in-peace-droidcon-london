package com.build38.droidconlondon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView

class MainActivity : AppCompatActivity(), ViewInterface {

    private val presenter = Presenter()

    private lateinit var bodyResponseTextView: TextView
    private lateinit var runNetworkCallButton: Button
    private lateinit var networkSecretsModeRadioGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bodyResponseTextView = findViewById(R.id.center_text)
        runNetworkCallButton = findViewById(R.id.button_run_call)
        networkSecretsModeRadioGroup = findViewById(R.id.radioGroup)

        runNetworkCallButton.setOnClickListener {
            val mode = when (networkSecretsModeRadioGroup.checkedRadioButtonId) {
                R.id.radio_without_cert_pinning -> ApiSecretsMode.WITHOUT_CERTIFICATE_PINNING
                R.id.radio_hardocded_token -> ApiSecretsMode.HARDCODED_API_SECRETS
                R.id.radio_protected_token -> ApiSecretsMode.PROTECTED_API_SECRETS
                else -> ApiSecretsMode.WITHOUT_CERTIFICATE_PINNING
            }
            presenter.onUserClickedRunNetworkCall(mode)
        }

        presenter.onViewAttached(this)
    }

    override fun showSuccessfulNetworkCallWith(secretsMode: ApiSecretsMode, body: String) {
        runOnUiThread {
            bodyResponseTextView.text = "Operation Mode: $secretsMode\n\nBody:\n\n$body"
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