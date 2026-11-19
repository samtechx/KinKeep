package app.kinkeep.data.db

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Creates and protects the database passphrase.
 *
 * A random 256-bit passphrase is generated once. It is stored only in encrypted form, wrapped with an
 * AES-GCM key that lives in the Android Keystore (hardware-backed where available) and never leaves it.
 */
@Singleton
class DatabaseKeyProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val prefs by lazy { context.getSharedPreferences(PREFS, Context.MODE_PRIVATE) }

    @Synchronized
    fun passphrase(): ByteArray {
        val stored = prefs.getString(KEY_WRAPPED, null)
        if (stored != null) return unwrap(Base64.decode(stored, Base64.NO_WRAP))

        val raw = ByteArray(PASSPHRASE_BYTES).also { SecureRandom().nextBytes(it) }
        val passphrase = raw.joinToString("") { "%02x".format(it) }.toByteArray(Charsets.US_ASCII)
        val wrapped = Base64.encodeToString(wrap(passphrase), Base64.NO_WRAP)
        prefs.edit().putString(KEY_WRAPPED, wrapped).commit()
        return passphrase
    }

    private fun wrappingKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        (keyStore.getKey(ALIAS, null) as? SecretKey)?.let { return it }

        val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        generator.init(
            KeyGenParameterSpec.Builder(
                ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build(),
        )
        return generator.generateKey()
    }

    private fun wrap(plain: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, wrappingKey())
        return cipher.iv + cipher.doFinal(plain)
    }

    private fun unwrap(blob: ByteArray): ByteArray {
        val iv = blob.copyOfRange(0, IV_BYTES)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, wrappingKey(), GCMParameterSpec(TAG_BITS, iv))
        return cipher.doFinal(blob, IV_BYTES, blob.size - IV_BYTES)
    }

    private companion object {
        const val PREFS = "kinkeep_keys"
        const val KEY_WRAPPED = "db_passphrase_wrapped"
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val ALIAS = "kinkeep_db_wrap_key"
        const val TRANSFORMATION = "AES/GCM/NoPadding"
        const val PASSPHRASE_BYTES = 32
        const val IV_BYTES = 12
        const val TAG_BITS = 128
    }
}
