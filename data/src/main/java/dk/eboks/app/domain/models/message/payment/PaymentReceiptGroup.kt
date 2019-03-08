package dk.eboks.app.domain.models.message.payment

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by bison on 24-06-2017.
 */
@Parcelize
data class PaymentReceiptGroup(
    var title: String?,
    var receiptLines: List<PaymentReceiptLine>

) : Parcelable