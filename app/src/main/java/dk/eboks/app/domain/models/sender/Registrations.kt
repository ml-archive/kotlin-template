package dk.eboks.app.domain.models.sender

import android.os.Parcelable
import dk.eboks.app.domain.models.shared.Status
import kotlinx.android.parcel.Parcelize

/**
 * Created by Christian on 3/28/2018.
 * @author   Christian
 * @since    3/28/2018.
 */
@Parcelize
data class Registrations(
        val senders : List<Sender>,
        val private : Status? = null,
        val public : Status? = null
) : Parcelable