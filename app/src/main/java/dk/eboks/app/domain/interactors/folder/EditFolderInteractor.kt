package dk.eboks.app.domain.interactors.folder

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface EditFolderInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(var folderId: Int?, var folderName: String?, var parentFolderId: Int?)

    interface Output {
        fun onEditFolderSuccess()
        fun onEditFolderError(error: ViewError)
    }
}