package dk.eboks.app.domain.models.channel

import com.google.gson.annotations.SerializedName

/**
 * Created by thsk on 19/02/2018.
 */
enum class RequirementType(val type : String) {
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