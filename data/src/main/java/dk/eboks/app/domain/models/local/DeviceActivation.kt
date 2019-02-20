package dk.eboks.app.domain.models.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.security.PrivateKey
import java.security.PublicKey

@Parcelize
data class DeviceActivation(
    var publicKey: PublicKey? = null,
    var privateKey: PrivateKey? = null,
    var deviceName: String? = null,
    var userId: String? = null
) : Parcelable