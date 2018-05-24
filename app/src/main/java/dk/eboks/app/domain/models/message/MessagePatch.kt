package dk.eboks.app.domain.models.message

import java.io.Serializable

/**
 * Created by bison on 24-06-2017.
 */
data class MessagePatch(
   var read : Boolean? = null,
   var archive : Boolean? = null,
   var parentFolderId : Int? = null,
   var note : String? = null
) : Serializable