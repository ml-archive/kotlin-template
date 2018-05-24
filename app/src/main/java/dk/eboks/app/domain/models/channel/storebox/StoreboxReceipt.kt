package dk.eboks.app.domain.models.channel.storebox

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class StoreboxReceipt(
        @SerializedName("id")
        var id: String = "",
        @SerializedName("merchantId")
        var merchantId: String = "",
        @SerializedName("purchaseDateTime")
        var purchaseDateTime: Date? = null,
        @SerializedName("grandTotal")
        var grandTotal: StoreboxReceiptPrice? = null,
        @SerializedName("merchant")
        var merchant: StoreboxMerchant? = null,
        @SerializedName("barcode")
        var barcode: StoreboxBarcode? = null,
        @SerializedName("optionals")
        var optionals: StoreboxOptionals? = null,
        @SerializedName("payments")
        var payments: ArrayList<StoreboxPayment> = arrayListOf(),
        @SerializedName("receiptLines")
        var receiptLines: ArrayList<StoreboxReceiptLine> = arrayListOf()
) : Parcelable {
    companion object {
        val KEY_ID = StoreboxReceipt::class.simpleName
    }
}