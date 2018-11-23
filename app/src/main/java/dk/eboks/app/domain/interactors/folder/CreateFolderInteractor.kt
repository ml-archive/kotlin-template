package dk.eboks.app.domain.interactors.folder

import dk.eboks.app.domain.models.folder.FolderRequest
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface CreateFolderInteractor : Interactor {
    var output : Output?
    var input : Input?

    data class Input(var folderRequest: FolderRequest)

    interface Output {
        fun onCreateFolderSuccess()
        fun onCreateFolderError(error : ViewError)
    }
}