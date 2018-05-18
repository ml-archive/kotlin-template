package dk.eboks.app.domain.models.shared

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by thsk on 28/03/2018.
 */

data class BooleanReply (
        var exists: Boolean = false
) : Serializable