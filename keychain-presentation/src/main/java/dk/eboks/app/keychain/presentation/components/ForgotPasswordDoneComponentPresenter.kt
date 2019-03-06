package dk.eboks.app.keychain.presentation.components

import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class ForgotPasswordDoneComponentPresenter @Inject constructor() :
    ForgotPasswordDoneComponentContract.Presenter,
    BasePresenterImpl<ForgotPasswordDoneComponentContract.View>() {

    init {
    }
}