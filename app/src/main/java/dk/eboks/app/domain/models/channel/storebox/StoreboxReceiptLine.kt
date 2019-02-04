package dk.eboks.app.domain.models.channel.storebox

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoreboxReceiptLine(
    @SerializedName("itemPrice")
    var itemPrice: StoreboxReceiptPrice? = null,
    @SerializedName("totalPrice")
    var totalPrice: StoreboxReceiptPrice? = null,
    @SerializedName("amount")
    var amount: Double? = null,
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("description")
    var description: String? = ""
) : Parcelable