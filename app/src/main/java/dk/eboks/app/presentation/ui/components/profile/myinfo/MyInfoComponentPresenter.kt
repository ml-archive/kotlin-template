package dk.eboks.app.presentation.ui.components.profile.myinfo

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

class MyInfoComponentPresenter @Inject constructor(val appState: AppStateManager) :
        MyInfoComponentContract.Presenter,
        BasePresenterImpl<MyInfoComponentContract.View>() {


}