package dk.eboks.app.domain.models.channel.ekey

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

interface BaseEkey : Parcelable {
    val name: String
    val note: String?
    val eKeyType: String
}

@Parcelize
data class Login(
    var username: String,
    var password: String,
    override val name: String,
    override val note: String?,
    override val eKeyType: String = "Login"
) : BaseEkey

@Parcelize
data class Note(
    override val name: String,
    override val note: String?,
    override val eKeyType: String = "Note"
) : BaseEkey

@Parcelize
data class Pin(
    override val name: String,
    var pin: String,
    override val note: String?,
    override val eKeyType: String = "Pin"
) : BaseEkey

@Parcelize
data class Ekey(
    var pin: String,
    override val name: String,
    override val note: String?,
    override val eKeyType: String = "Ekey"
) : BaseEkey