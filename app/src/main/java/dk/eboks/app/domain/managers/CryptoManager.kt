package dk.eboks.app.domain.managers

import dk.eboks.app.domain.models.local.DeviceActivation
import java.security.PrivateKey
import java.security.PublicKey

interface CryptoManager {
    @Throws(Exception::class) fun generateRSAKey(userId: String)
    fun saveActivation(userId: String)
    fun loadActivation(userId: String): Boolean
    fun hasActivation(userId: String): Boolean
    @Throws(Exception::class) fun hashStringData(data: String, pk: PrivateKey): String
    fun getPublicKeyAsString(publicKey: PublicKey): String
    fun getPrivateKeyAsString(privateKey: PrivateKey): String
    fun getActivation(userId: String): DeviceActivation?
    @Throws(Exception::class) fun deleteActivation(userId: String)
    @Throws(Exception::class) fun deleteAllActivations()
}