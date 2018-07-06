package dk.eboks.app.presentation.ui.senders.components.categories

import dk.eboks.app.domain.models.SenderCategory
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

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
        fun getCategories()
    }
}