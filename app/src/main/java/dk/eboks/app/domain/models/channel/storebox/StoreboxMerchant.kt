package dk.eboks.app.domain.models.channel.storebox

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoreboxMerchant(
        @SerializedName("logo")
        var logo: StoreboxMerchantLogo? = null,
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("addressLine1")
        var addressLine1: String? = null,
        @SerializedName("addressLine2")
        var addressLine2: String? = null,
        @SerializedName("zipCode")
        var zipCode: String? = null,
        @SerializedName("phoneNumber")
        var phoneNumber: String? = null
) : Parcelable