package dk.eboks.app.domain.models.message

import android.os.Parcelable
import dk.eboks.app.domain.models.shared.Description
import dk.eboks.app.domain.models.shared.Status
import kotlinx.android.parcel.Parcelize
import java.util.*

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
    var notfication: Boolean,
    var cancel: Int?

) : Parcelable