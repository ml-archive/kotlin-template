package dk.eboks.app.system.managers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.security.KeyPairGeneratorSpec
import android.util.Base64
import dk.eboks.app.domain.managers.CryptoManager
import dk.eboks.app.domain.models.local.DeviceActivation
import dk.eboks.app.domain.repositories.SettingsRepository
import timber.log.Timber
import java.math.BigInteger
import java.security.*
import java.util.*
import javax.inject.Inject
import javax.security.auth.x500.X500Principal

class CryptoManagerImpl @Inject constructor(val context: Context, val settingsRepository: SettingsRepository) : CryptoManager {
    private val AndroidKeyStore = "AndroidKeyStore"

    @SuppressLint("NewApi")
    @Throws(Exception::class)
    override fun generateRSAKey(userId: String) {
        val keyStore = KeyStore.getInstance(AndroidKeyStore)
        keyStore.load(null)
        // Generate the RSA key pairs
        if (!keyStore.containsAlias(userId)) {
            // Generate a key pair for encryption
            val start = Calendar.getInstance()
            val end = Calendar.getInstance()
            end.add(Calendar.YEAR, 30)

            val spec = KeyPairGeneratorSpec.Builder(context)
                    .setAlias(userId)
                    .setSubject(X500Principal("CN=$userId"))
                    .setSerialNumber(BigInteger.TEN)
                    .setStartDate(start.time)
                    .setKeySize(2048)
                    .setEndDate(end.time)
                    .build()

            val kpg = KeyPairGenerator.getInstance("RSA", AndroidKeyStore)
            kpg.initialize(spec)
            val kp = kpg.generateKeyPair()
            Timber.e("Public key: " + getPublicKeyAsString(kp.public))
            //NLog.e(TAG, "Private key: " + getPrivateKeyAsString(kp.getPrivate()));
        }
    }

    /*
        Empty because the android key store saves the key when its generated above
     */
    override fun saveActivation(userId: String) {

    }

    override fun loadActivation(userId: String): Boolean {
        return hasActivation(userId)
    }

    override fun hasActivation(userId: String): Boolean {
        try {
            val keyStore = KeyStore.getInstance(AndroidKeyStore)
            keyStore.load(null)
            if (keyStore.containsAlias(userId))
                return true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    @Throws(Exception::class)
    override fun hashStringData(data: String, pk: PrivateKey): String {
        val byteData = data.toByteArray(charset("UTF-8"))
        val sig = Signature.getInstance("SHA256withRSA")
        sig.initSign(pk)
        sig.update(byteData)
        val signatureBytes = sig.sign()
        val result = Base64.encodeToString(signatureBytes, Base64.NO_WRAP)
        Timber.e("RSAHash: $result")
        return result
    }

    override fun getPublicKeyAsString(publicKey: PublicKey): String {
        val encodedPublicKey = publicKey.encoded
        return Base64.encodeToString(encodedPublicKey, Base64.NO_WRAP)
    }

    override fun getPrivateKeyAsString(privateKey: PrivateKey): String {
        val encodedPrivateKey = privateKey.encoded
        return Base64.encodeToString(encodedPrivateKey, Base64.NO_WRAP)
    }

    override fun getActivation(userId: String): DeviceActivation? {
        try {
            if (hasActivation(userId)) {
                return DeviceActivation(getPublicKeyFromKeystore(userId), getPrivateKeyFromKeystore(userId), settingsRepository.get().deviceId, userId)
            } else {
                Timber.e("No keypair found for user id $userId")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    @Throws(Exception::class)
    override fun deleteActivation(userId: String) {
        val keyStore = KeyStore.getInstance(AndroidKeyStore)
        keyStore.load(null)
        keyStore.deleteEntry(userId)
        Timber.e("Deleting entry $userId")
    }

    @Throws(Exception::class)
    override fun deleteAllActivations() {
        val keyStore = KeyStore.getInstance(AndroidKeyStore)
        keyStore.load(null)

        val aliases = keyStore.aliases()
        while (aliases.hasMoreElements()) {
            val alias = aliases.nextElement()
            Timber.e("Deleting Alias: $alias")
            keyStore.deleteEntry(alias)
        }
    }

    /**
     * Private utility functions
     */

    @Throws(Exception::class)
    private fun getPrivateKeyFromKeystore(userId: String): PrivateKey {
        val keyStore = KeyStore.getInstance(AndroidKeyStore)
        keyStore.load(null)
        /*
        Enumeration<String> aliases = keyStore.aliases();
        while(aliases.hasMoreElements())
        {
            NLog.e(TAG, "Alias: " + aliases.nextElement());
        }
        */
        val privateKeyEntry = keyStore.getEntry(userId, null) as KeyStore.PrivateKeyEntry
        return privateKeyEntry.privateKey
    }

    @Throws(Exception::class)
    private fun getPublicKeyFromKeystore(userId: String): PublicKey {
        val keyStore = KeyStore.getInstance(AndroidKeyStore)
        keyStore.load(null)
        val privateKeyEntry = keyStore.getEntry(userId, null) as KeyStore.PrivateKeyEntry
        return privateKeyEntry.certificate.publicKey
    }
}