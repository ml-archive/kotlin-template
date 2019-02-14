package dk.eboks.app.presentation.ui.profile.screens.myinfo

import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class MyInfoPresenter @Inject constructor() : MyInfoContract.Presenter,
    BasePresenterImpl<MyInfoContract.View>() {
    init {
    }
}