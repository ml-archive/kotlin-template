package dk.eboks.app.domain.models.message.payment

import android.os.Parcelable
import dk.eboks.app.domain.models.shared.Status
import kotlinx.android.parcel.Parcelize

/**
 * Created by bison on 24-06-2017.
 */
@Parcelize
data class Payment(
        var status: Status,
        var options: List<PaymentOption>? = null,
        var disclaimer: String?,
        var receipt: List<PaymentReceiptGroup>? = null,
        val amount: PaymentAmount? = null,
        var notification: Boolean,
        var cancel: Int?

) : Parcelable