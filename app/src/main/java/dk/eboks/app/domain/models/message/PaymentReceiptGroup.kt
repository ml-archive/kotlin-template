package dk.eboks.app.domain.models.message

import java.io.Serializable

/**
 * Created by bison on 24-06-2017.
 */
data class PaymentReceiptGroup(
        var title : String?,
        var receiptLines : List<PaymentReceiptLine>

) : Serializable