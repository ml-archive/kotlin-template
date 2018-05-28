package dk.eboks.app.domain.models.channel.storebox

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoreboxMerchantLogo(
        @SerializedName("url")
        var url: String? = "",
        @SerializedName("text")
        var text: String? = "",
        @SerializedName("version")
        var version: String? = ""
) : Parcelable