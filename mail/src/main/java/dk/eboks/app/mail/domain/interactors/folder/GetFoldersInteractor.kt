package dk.eboks.app.mail.domain.interactors.folder

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.presentation.ui.folder.components.FolderMode
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface GetFoldersInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(val cached: Boolean, val pickermode: FolderMode, val userId: Int?)

    interface Output {
        fun onGetFolders(folders: List<Folder>)
        fun onGetSystemFolders(folders: List<Folder>)
        fun onGetFoldersError(error: ViewError)
    }
}