package dk.eboks.app.domain.models.channel.storebox

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoreboxMerchantLogo(
        @SerializedName("url")
        var url: String? = null,
        @SerializedName("text")
        var text: String? = null,
        @SerializedName("version")
        var version: String? = null
) : Parcelable