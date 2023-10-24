package com.build38.droidconlondon

import android.util.Log
import com.build38.droidconlondon.MainActivity.Companion.LOG_TAG
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

private const val TAG_LENGTH = 16

internal class EncryptedSecretsProvider: ApiSecretsProvider {

    /*
     * Secrets needed for token decryption.
     *
     * iv, key and aad can be set at user discretion.
     *
     * tag and ciphertext are the result of the encryption of the desired token with the give iv, key and aad.
     *
     * Any change in the token, iv, key or aad means tag and ciphertext need to be re-generated. The auxilary function
     * [generateEncryptedToken] can be used for that
     */

    private val iv = sha256(BuildConfig.APPLICATION_ID + BuildConfig.VERSION_NAME)
    private val key = byteArrayOf(0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F)
    private val aad = byteArrayOf(0x0F, 0x0E, 0x0D, 0x0C, 0x0B, 0x0A, 0x09, 0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00)
    private val tag = byteArrayOf(0x85.toByte(), 0xb8.toByte(), 0x75, 0x61, 0x2b, 0xc8.toByte(), 0x37, 0x9e.toByte(), 0x87.toByte(), 0x42, 0x31, 0x06, 0x2e, 0xa8.toByte(), 0x40, 0x9a.toByte())
    private val ciphertext = byteArrayOf(0xa1.toByte(), 0xd1.toByte(), 0x75, 0x8b.toByte(), 0xad.toByte(), 0x89.toByte(), 0xb1.toByte(), 0xe1.toByte(), 0x98.toByte(), 0xbe.toByte(), 0xf2.toByte(), 0xcd.toByte(), 0x83.toByte(), 0x5a, 0xe2.toByte(), 0xee.toByte(), 0x99.toByte(), 0x46, 0x52, 0xa0.toByte(), 0x97.toByte(), 0x06, 0x99.toByte(), 0x0b, 0x25, 0x74, 0x82.toByte(), 0x98.toByte(), 0x76)

    override fun retrieveApiSecrets(baseUrl: String): String {
        return if (baseUrl.endsWith("httpbin.org")) {
            // Un-comment the following line if you need to re-generate ciphertext/tag
            // generateEncryptedToken("EncryptedToken_dr0idConL0Ndon")
            decryptSecretToken()
        } else {
            throw UnsupportedOperationException("Unknown Base URL $baseUrl")
        }

    }

    private fun decryptSecretToken(): String {
        return aes256Decrypt(iv, aad, key, ciphertext, tag)
    }

    private fun sha256(input: String): ByteArray  = MessageDigest.getInstance("SHA256").digest(input.toByteArray())

    private fun aes256Decrypt(iv: ByteArray, aad: ByteArray, keyBytes: ByteArray, ciphertext: ByteArray, tag: ByteArray): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(TAG_LENGTH * 8, iv)
        val key = SecretKeySpec(keyBytes, 0, keyBytes.size, "AES")
        cipher.init(Cipher.DECRYPT_MODE, key, spec)
        cipher.updateAAD(aad)
        return String(cipher.doFinal(ciphertext + tag))
    }

    /**
     * Aux function to re-create ciphertext and tag
     */
    private fun generateEncryptedToken(token: String) {
        val token = aes256Encrypt(iv, aad, key, token)
        logHex("Authentication Tag", token.first)
        logHex("Ciphertext", token.second)
    }

    private fun aes256Encrypt(iv: ByteArray, aad: ByteArray, keyBytes: ByteArray, cleartext: String): Pair<ByteArray, ByteArray> {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(TAG_LENGTH * 8, iv)
        val key = SecretKeySpec(keyBytes, 0, keyBytes.size, "AES")
        cipher.init(Cipher.ENCRYPT_MODE, key, spec)
        cipher.updateAAD(aad)
        val result = cipher.doFinal(cleartext.toByteArray())
        val ciphertext = result.copyOfRange(0, result.size - TAG_LENGTH)
        val tag = result.copyOfRange(result.size - TAG_LENGTH, result.size)
        return Pair(tag, ciphertext)
    }

    private fun logHex(name: String, byteArray: ByteArray) {
        val hexString = byteArray.joinToString(separator = ", ") { eachByte -> "0x%02x".format(eachByte) }
        Log.d(LOG_TAG, "$name: $hexString")
    }
}
