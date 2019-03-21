package dk.eboks.app.mail.domain.interactors.categories

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface GetCategoriesInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(val cached: Boolean, val userId: Int?)

    interface Output {
        fun onGetCategories(folders: List<Folder>)
        fun onGetCategoriesError(error: ViewError)
    }
}