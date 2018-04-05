package dk.eboks.app.presentation.ui.components.home

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class HomeComponentPresenter @Inject constructor(val appState: AppStateManager) : HomeComponentContract.Presenter, BasePresenterImpl<HomeComponentContract.View>() {

    init {
    }

    override fun setup() {
        appState.state?.currentUser?.let { user->
            runAction { v->
                v.verifiedUser = user.verified
                v.setupViews()
            }
        }
    }
}