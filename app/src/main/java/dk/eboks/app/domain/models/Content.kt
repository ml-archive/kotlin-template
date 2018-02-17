package dk.eboks.app.domain.models

import java.io.Serializable

/**
 * Created by bison on 24-06-2017.
 */
data class Content(
   var id : String,
   var title : String,
   var fileSize : Long,
   var fileExtension : String,
   var mimeType : String?,
   var encoding : String?
) : Serializable