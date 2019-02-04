package dk.eboks.app.domain.models.login

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class SharedUser(
    var id: Int,
    var userId: Int,
    var name: String,
    var permission: String,
    var expiryDate: Date?,
    var status: String?

) : Parcelable