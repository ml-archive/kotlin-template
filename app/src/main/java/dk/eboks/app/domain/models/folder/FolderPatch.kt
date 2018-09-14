package dk.eboks.app.domain.models.folder

import java.io.Serializable

data class FolderPatch(
        var userId : Int? = null,
        var parentFolderId : Int? = null,
        var name : String? = null
) : Serializable