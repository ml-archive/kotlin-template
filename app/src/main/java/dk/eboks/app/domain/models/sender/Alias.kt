package dk.eboks.app.domain.models.sender

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Alias(val name: String, val key: String? = "", var value: String? = "") : Parcelable {

    override fun toString(): String {
        return key ?: ""
    }
}