package dk.eboks.app.domain.models.channel.storebox

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoreboxReceiptPrice(
    @SerializedName("value")
    var value: Double? = null,
    @SerializedName("vat")
    var vat: Double? = null
) : Parcelable