package dk.eboks.app.domain.models.channel

import android.graphics.Color
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by thsk on 19/02/2018.
 */
@Parcelize
data class ChannelColor(
        val rgb: String? = null,
        val rgba: String? = null
) : Parcelable {
    // I'm not proud of this
    val color: Int
        get() {
            if (rgb != null) {
                if (!rgb.contains("#")) {
                    return Color.parseColor("#$rgb")
                }
                return Color.parseColor(rgb)
            }

            if (rgba != null) {
                return Color.parseColor(rgba)
            }

            return Color.WHITE
        }
}