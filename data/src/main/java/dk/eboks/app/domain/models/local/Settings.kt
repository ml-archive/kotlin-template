package dk.eboks.app.domain.models.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Settings(
    var deviceId: String
) : Parcelable
