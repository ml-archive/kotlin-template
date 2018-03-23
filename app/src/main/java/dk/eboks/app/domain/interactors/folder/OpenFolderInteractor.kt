package dk.eboks.app.domain.interactors.folder

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface OpenFolderInteractor : Interactor {
    var output : Output?
    var input : Input?

    data class Input(var folder : Folder)

    interface Output {
        fun onOpenFolderDone()
        fun onOpenFolderError(error : ViewError)
    }
}