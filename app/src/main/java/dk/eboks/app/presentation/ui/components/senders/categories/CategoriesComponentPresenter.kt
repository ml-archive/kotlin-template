package dk.eboks.app.presentation.ui.components.senders.categories

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.SenderCategory
import dk.nodes.arch.presentation.base.BasePresenterImpl
import kotlinx.coroutines.experimental.channels.Send
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 * Copied by chnt on 12-03-2018
 */
class CategoriesComponentPresenter @Inject constructor(val appState: AppStateManager) : CategoriesComponentContract.Presenter, BasePresenterImpl<CategoriesComponentContract.View>() {

    init {
        getCategories()
    }

    fun getCategories() {
        val cats = ArrayList<SenderCategory>()
        for(i in 0..30) {
            cats.add(SenderCategory(i.toLong(), "Cat-$i", (Math.random()*100).toInt()))
        }
        runAction { v ->
            v.showCategories(cats)
        }
    }
}