package dk.eboks.app.domain.models.home

import android.os.Parcelable
import dk.eboks.app.domain.models.Image
import dk.eboks.app.domain.models.shared.Status
import kotlinx.android.parcel.Parcelize
import java.util.Date

/**
 * Created by thsk on 16/02/2018.
 */
@Parcelize
data class Item(
        var id : String,
        var title : String?,
        var description: String?,
        var date: Date?,
        var amount: Double?,
        //var currency: String?,
        var status : Status?,
        var tag : String?,
        var image : Image?
) : Parcelable