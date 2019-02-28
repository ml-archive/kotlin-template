package dk.eboks.app.domain.models.message.payment

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaymentAmount(
        val value: Double,
        val currency: String
) : Parcelable {

    override fun toString(): String {
        return "$value $currency"
    }
}
