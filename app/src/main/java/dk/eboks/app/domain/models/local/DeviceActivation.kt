package dk.eboks.app.domain.models.local

import java.io.Serializable
import java.security.PrivateKey
import java.security.PublicKey

data class DeviceActivation (
    var publicKey: PublicKey? = null,
    var privateKey: PrivateKey? = null,
    var deviceName: String? = null,
    var userId : String? = null
) : Serializable