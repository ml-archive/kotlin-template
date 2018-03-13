package dk.eboks.app.domain.interactors.sender

import dk.eboks.app.domain.models.SenderCategory
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by Christian on 3/13/2018.
 * @author   Christian
 * @since    3/13/2018.
 */
interface GetSenderCategoriesInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(val cached: Boolean)

    interface Output {
        fun onGetCategories(categories: List<SenderCategory>)
        fun onGetCategoriesError(msg : String)
    }
}