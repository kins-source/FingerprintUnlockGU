package com.example.fingerprintunlock

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.fingerprintunlock.databinding.ActivityMainBinding

/**
 * On-screen "unlock by fingerprint" button.
 *
 * Uses AndroidX BiometricPrompt, which is the correct, future-proof API for
 * this on both stock Android and Samsung devices — Samsung's fingerprint
 * sensors (side-mounted or in-display, e.g. Galaxy S/Note/A series) are
 * exposed to apps through the same standard framework API, so no
 * Samsung-specific SDK or branch is needed.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fingerprintButton.setOnClickListener {
            verifyFingerprint()
        }
    }

    private fun verifyFingerprint() {
        val biometricManager = BiometricManager.from(this)

        // 1. Check the device actually supports & has fingerprint enrolled.
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> { /* good to go */ }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                toast("This device has no fingerprint sensor")
                return
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                toast("Fingerprint sensor is currently unavailable")
                return
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                toast("No fingerprint enrolled — add one in device Settings")
                return
            }
            else -> {
                toast("Fingerprint unlock is not available")
                return
            }
        }

        // 2. Build the prompt.
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(
            this,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    onUnlocked()
                }

                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    toast("Error: $errString")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    toast("Fingerprint not recognized — try again")
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Verify your fingerprint")
            .setSubtitle("Touch the sensor to unlock")
            .setNegativeButtonText("Cancel")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun onUnlocked() {
        binding.statusText.text = "Unlocked"
        binding.hintText.text = "Fingerprint verified"
        toast("Unlocked")
        // TODO: put your real unlock action here —
        // e.g. navigate to the next screen, decrypt content, etc.
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
