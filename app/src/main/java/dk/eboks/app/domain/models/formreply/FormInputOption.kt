package dk.eboks.app.domain.models.formreply

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FormInputOption (
   var name : String,
   var value : String
) : Parcelable
