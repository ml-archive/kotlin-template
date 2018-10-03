package dk.eboks.app.domain.models.folder

import java.io.Serializable

data class FolderRequest(
        var userId : Int?,
        var parentFolderId : Int,
        var name : String
) : Serializable