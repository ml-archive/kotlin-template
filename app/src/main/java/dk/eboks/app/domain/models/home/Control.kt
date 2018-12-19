package dk.eboks.app.domain.models.home

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by thsk on 16/02/2018.
 */
@Parcelize
data class Control(
        var id : String,
        var type : ItemType,
        var items : List<Item>? = null
) : Parcelable