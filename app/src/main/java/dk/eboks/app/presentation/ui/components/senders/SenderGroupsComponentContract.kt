package dk.eboks.app.presentation.ui.components.senders.categories

import dk.eboks.app.domain.models.SenderCategory
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 * Copied by chnt on 12-03-2018
 */
interface CategoriesComponentContract {
    interface View : BaseView {
        fun showCategories(categories: List<SenderCategory>)
        fun showError(message: String)
    }

    interface Presenter : BasePresenter<View> {
    }
}