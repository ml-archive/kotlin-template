package dk.eboks.app.domain.models.channel

import dk.eboks.app.domain.models.Image
import dk.eboks.app.domain.models.shared.Currency
import java.io.Serializable
import java.util.*

/**
 * Created by thsk on 16/04/2018.
 */
data class StoreboxReceipt(

        var id: String,
        var storeName: String,
        var purchaseDate: Date?,
        var grandTotal: Currency,
        var logo: Image
) : Serializable