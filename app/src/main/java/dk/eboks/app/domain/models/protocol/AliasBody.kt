package dk.eboks.app.domain.models.protocol

import android.os.Parcelable
import dk.eboks.app.domain.models.sender.Alias
import kotlinx.android.parcel.Parcelize

/**
 * Created by Christian on 3/23/2018.
 * @author   Christian
 * @since    3/23/2018.
 */
@Parcelize
data class AliasBody(val aliasRegistrations: List<Alias>?) : Parcelable