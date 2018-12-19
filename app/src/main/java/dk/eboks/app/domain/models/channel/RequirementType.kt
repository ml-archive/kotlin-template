package dk.eboks.app.domain.models.channel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by thsk on 19/02/2018.
 */
@Parcelize
enum class RequirementType(val type : String): Parcelable {
    @SerializedName("identity") IDENTITY("identitiy"),
    @SerializedName("name") NAME("name"),
    @SerializedName("email") EMAIL("email"),
    @SerializedName("mobilnummer") MOBILNUMMER("mobilnummer");

    override fun toString(): String {
        when(this)
        {
            IDENTITY -> return "identity"
            NAME -> return "name"
            EMAIL -> return "email"
            else -> return "mobilnummer"
        }
    }
}