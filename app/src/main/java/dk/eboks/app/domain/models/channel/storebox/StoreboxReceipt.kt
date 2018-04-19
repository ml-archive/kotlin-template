package dk.eboks.app.domain.models.channel.storebox

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class StoreboxReceipt(
        @SerializedName("id")
        var id: String,
        @SerializedName("merchantId")
        var merchantId: String,
        @SerializedName("grantTotal")
        var grantTotal: StoreboxReceiptPrice? = null,
        @SerializedName("purchaseDateTime")
        var purchaseDateTime: Date? = null,
        @SerializedName("merchant")
        var merchant: StoreboxMerchant? = null,
        @SerializedName("receiptLines")
        var receiptLines: StoreboxReceiptLines? = null,
        @SerializedName("payments")
        var payments: MutableList<StoreboxPayment>? = arrayListOf(),
        @SerializedName("barcode")
        var barcode: StoreboxBarcode? = null,
        @SerializedName("optionals")
        var optionals: StoreboxOptionals? = null
) : Parcelable