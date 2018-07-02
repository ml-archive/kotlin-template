package dk.eboks.app.domain.models.channel.storebox

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoreboxOptionals(
        @SerializedName("headerText")
        var headerText: String? = "",
        @SerializedName("footerText")
        var footerText: String? = "",
        @SerializedName("orderNumber")
        var orderNumber: String? = "",
        @SerializedName("storeRegNumber")
        var storeRegNumber: String? = ""
) : Parcelable