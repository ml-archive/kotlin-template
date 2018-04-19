package dk.eboks.app.domain.models.shared

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by thsk on 28/03/2018.
 */

data class Currency(
        @SerializedName("value")
        var value: Double?,
        @SerializedName("currency")
        var currency: String?
) : Serializable