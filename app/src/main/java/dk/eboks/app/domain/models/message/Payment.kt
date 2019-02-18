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
    var amount: Double?,
    var currency: String?,
    var notfication: Boolean,
    var cancel: Int?

) : Parcelable {


    companion object {

        private const val dis = "Disclaimer {sender-name} lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur dictum scelerisque orci, sit amet auctor nulla. Nam ac odio sagittis, sodales ante accumsan, commodo sapien."

        fun mock() : Payment {
            val status = Status(type = 1, date = Date())
            val options = List(3) { index ->
                PaymentOption("Berlin Service", Description("tex", "tiltle"), 1, PaymentOption.type(index))
            }
            return Payment(status, options, dis, amount = 1321.0, currency = "US Dollars", notfication = true, cancel = 1)
        }
    }

}