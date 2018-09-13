package dk.eboks.app.domain.models.message

import java.io.Serializable

/**
 * Created by bison on 24-06-2017.
 */
data class MessagePatch(
   var unread : Boolean? = null,
   var archive : Boolean? = null,
   var folderId : Int? = null,
   var note : String? = null
) : Serializable