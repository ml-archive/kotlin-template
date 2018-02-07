package dk.eboks.app.domain.interactors

import dk.eboks.app.domain.models.Folder
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface GetCategoriesInteractor : Interactor {
    var output : Output?
    var input : Input?

    data class Input(val cached: Boolean)

    interface Output {
        fun onGetCategories(folders : List<Folder>)
        fun onGetCategoriesError(msg : String)
    }
}