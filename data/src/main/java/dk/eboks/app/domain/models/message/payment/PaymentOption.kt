package dk.eboks.app.domain.models.message.payment

import android.os.Parcelable
import dk.eboks.app.domain.models.shared.Description
import kotlinx.android.parcel.Parcelize

/**
 * Created by bison on 24-06-2017.
 */
@Parcelize
data class PaymentOption(
    var name: String,
    var status: Int?,
    var type: String //  "type": (string) = ["betalingsservice","dibs","reepay"]

) : Parcelable