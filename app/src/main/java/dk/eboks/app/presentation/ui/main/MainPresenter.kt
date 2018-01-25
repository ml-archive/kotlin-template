package dk.eboks.app.presentation.ui.main

import dk.eboks.app.domain.interactors.LoginInteractor
import dk.nodes.arch.presentation.base.BasePresenterImpl

/**
 * Created by bison on 20-05-2017.
 */
class MainPresenter(val loginInteractor: LoginInteractor) : dk.eboks.app.presentation.ui.main.MainContract.Presenter, BasePresenterImpl<MainContract.View>(), LoginInteractor.Output {
    init {
        loginInteractor.output = this
        //loginInteractor.run()
    }

    override fun onLogin() {

    }

    override fun onLoginError(msg: String) {
        runAction{ view?.showError(msg) }
    }
}