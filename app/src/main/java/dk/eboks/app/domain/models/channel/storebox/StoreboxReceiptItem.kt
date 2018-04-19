package dk.eboks.app.domain.models.channel.storebox

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import dk.eboks.app.domain.models.shared.Currency
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class StoreboxReceiptItem(
        @SerializedName("id")
        var id: String,
        @SerializedName("storeName")
        var storeName: String,
        @SerializedName("purchaseDate")
        var purchaseDate: Date? = null,
        @SerializedName("grandTotal")
        var grandTotal: Currency? = null,
        @SerializedName("logo")
        var logo: StoreboxMerchantLogo? = null
) : Parcelable