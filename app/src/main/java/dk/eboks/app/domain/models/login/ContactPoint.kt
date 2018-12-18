package dk.eboks.app.domain.models.login

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContactPoint(
        var value: String? = "",
        var verified: Boolean = false
):Parcelable