package com.build38.droidconlondon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView

class MainActivity : AppCompatActivity(), ViewInterface {

    private var presenter = Presenter()

    private var mode = ApiSecretsMode.WITHOUT_CERTIFICATE_PINNING

    private lateinit var textViewOnUi: TextView
    private lateinit var buttonRunCallOnUi: Button
    private lateinit var radioGroupOnUi: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewOnUi = findViewById<TextView>(R.id.center_text)
        buttonRunCallOnUi = findViewById<Button>(R.id.button_run_call)
        radioGroupOnUi = findViewById(R.id.radioGroup)

        radioGroupOnUi.setOnCheckedChangeListener { group, checkedId ->
            // Handle the radio button selection here
            when (checkedId) {
                R.id.radio_without_cert_pinning -> {
                    mode = ApiSecretsMode.WITHOUT_CERTIFICATE_PINNING
                }
                R.id.radio_hardocded_token -> {
                    mode = ApiSecretsMode.HARDCODED_API_SECRETS
                }
                R.id.radio_protected_token -> {
                    mode = ApiSecretsMode.PROTECTED_API_SECRETS
                }
            }
        }

        presenter.onViewAttached(this)

        buttonRunCallOnUi.setOnClickListener {
            presenter.onUserClickedRunNetworkCall(mode)
        }
    }

    override fun showSuccessfulNetworkCallWith(secretsMode: ApiSecretsMode, body: String) {
        runOnUiThread {
            textViewOnUi.text = "Secret mode: $secretsMode and body is: \n\n $body"
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