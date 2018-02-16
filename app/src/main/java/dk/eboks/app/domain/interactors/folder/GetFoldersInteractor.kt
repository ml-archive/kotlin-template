package dk.eboks.app.domain.interactors.folder

import dk.eboks.app.domain.models.Folder
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface GetFoldersInteractor : Interactor {
    var output : Output?
    var input : Input?

    data class Input(val cached: Boolean)

    interface Output {
        fun onGetFolders(folders : List<Folder>)
        fun onGetSystemFolders(folders : List<Folder>)
        fun onGetFoldersError(msg : String)
    }
}