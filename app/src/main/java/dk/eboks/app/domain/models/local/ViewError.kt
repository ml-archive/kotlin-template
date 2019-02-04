package dk.eboks.app.domain.models.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ViewError(
    var title: String? = null,
    var message: String? = null,
    var shouldDisplay: Boolean = true,
    var shouldCloseView: Boolean = false
) : Parcelable
