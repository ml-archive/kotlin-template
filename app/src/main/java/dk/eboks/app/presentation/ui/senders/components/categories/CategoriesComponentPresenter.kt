package dk.eboks.app.presentation.ui.senders.components.categories

import dk.eboks.app.domain.senders.interactors.GetSenderCategoriesInteractor
import dk.eboks.app.domain.models.SenderCategory
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 * Copied by chnt on 12-03-2018
 */
class CategoriesComponentPresenter @Inject constructor(private val getSenderCategoriesInteractor: GetSenderCategoriesInteractor) :
    CategoriesComponentContract.Presenter, BasePresenterImpl<CategoriesComponentContract.View>(),
    GetSenderCategoriesInteractor.Output {

    override fun getCategories() {
        getSenderCategoriesInteractor.input = GetSenderCategoriesInteractor.Input(true)
        getSenderCategoriesInteractor.output = this
        getSenderCategoriesInteractor.run()
    }

    override fun onGetCategories(categories: List<SenderCategory>) {
        runAction { v ->
            v.showCategories(categories)
        }
    }

    override fun onGetCategoriesError(error: ViewError) {
        runAction { v ->
            v.showErrorDialog(error)
        }
    }
}