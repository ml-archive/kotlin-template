package dk.eboks.app.domain.models.channel.storebox

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoreboxReceiptLines(
        @SerializedName("items")
        var items: MutableList<StoreboxReceiptLine> = arrayListOf(),
        @SerializedName("total")
        var total: Double? = null
) : Parcelable