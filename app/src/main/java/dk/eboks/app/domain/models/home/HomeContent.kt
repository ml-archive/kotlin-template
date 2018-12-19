package dk.eboks.app.domain.models.home

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by thsk (not really, I was just to lazy to change it haha) on 16/02/2018.
 */
@Parcelize
data class HomeContent(
    var control: Control,
    var version: Int
) : Parcelable