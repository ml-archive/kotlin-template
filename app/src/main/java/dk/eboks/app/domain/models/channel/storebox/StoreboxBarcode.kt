package dk.eboks.app.domain.models.channel.storebox

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoreboxBarcode(
        @SerializedName("type")
        var type: String? = null,
        @SerializedName("value")
        var value: String? = null,
        @SerializedName("displayValue")
        var displayValue: String? = null
) : Parcelable