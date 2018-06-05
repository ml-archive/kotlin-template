package dk.eboks.app.domain.models.channel.storebox

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoreboxProfile(
    @SerializedName("greenProfile")
    var greenProfile: Boolean = false
) : Parcelable