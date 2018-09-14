package dk.eboks.app.domain.interactors.folder

import dk.eboks.app.domain.models.folder.FolderPatch
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface UpdateFolderInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(var folderId: Int, var folderPatch: FolderPatch)

    interface Output {
        fun onUpdateFolderSuccess()
        fun onUpdateFolderError(error: ViewError)
    }
}