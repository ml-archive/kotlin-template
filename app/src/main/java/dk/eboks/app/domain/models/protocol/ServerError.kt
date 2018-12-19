package dk.eboks.app.domain.models.protocol

import android.os.Parcelable
import dk.eboks.app.domain.models.shared.Description
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ServerError(
        var id : String?,
        var type : ErrorType,
        var code : Int = -1,
        var description: Description? = null,
        var debug : String? = null
) : Parcelable