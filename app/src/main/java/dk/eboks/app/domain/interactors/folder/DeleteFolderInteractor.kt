package dk.eboks.app.domain.interactors.folder

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface DeleteFolderInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(var folderId: Int)

    interface Output {
        fun onDeleteFolderSuccess()
        fun onDeleteFolderError(error: ViewError)
    }
}