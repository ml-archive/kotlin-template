package dk.eboks.app.profile.presentation.ui.screens

import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class MyInfoPresenter @Inject constructor() : MyInfoContract.Presenter,
    BasePresenterImpl<MyInfoContract.View>() {
    init {
    }
}