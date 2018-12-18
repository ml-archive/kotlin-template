package dk.eboks.app.domain.models.message

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by bison on 24-06-2017.
 */
@Parcelize
data class Content(
   var id : String,
   var title : String,
   var fileSize : Long,
   var fileExtension : String,
   var mimeType : String?,
   var encoding : String?,
   var contentUrlMock : String?
) : Parcelable