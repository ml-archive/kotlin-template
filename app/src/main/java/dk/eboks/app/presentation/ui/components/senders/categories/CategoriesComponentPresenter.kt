package dk.eboks.app.presentation.ui.components.senders.categories

import dk.eboks.app.domain.interactors.sender.GetSenderCategoriesInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.SenderCategory
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 * Copied by chnt on 12-03-2018
 */
class CategoriesComponentPresenter @Inject constructor(val appState: AppStateManager, val getSenderCategoriesInteractor: GetSenderCategoriesInteractor) :
        CategoriesComponentContract.Presenter, BasePresenterImpl<CategoriesComponentContract.View>(),
        GetSenderCategoriesInteractor.Output {

    init {
        getSenderCategoriesInteractor.output = this
        getSenderCategoriesInteractor.run()
    }

    override fun onGetCategories(categories: List<SenderCategory>) {
        runAction { v ->
            v.showCategories(categories)
        }
    }

    override fun onGetCategoriesError(error : ViewError) {
        runAction { v ->
            v.showErrorDialog(error)
        }
    }
}