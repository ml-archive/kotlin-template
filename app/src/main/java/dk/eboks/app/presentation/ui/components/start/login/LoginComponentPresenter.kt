package dk.eboks.app.presentation.ui.components.start.login

import dk.eboks.app.domain.interactors.user.CreateUserInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.internal.User
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class LoginComponentPresenter @Inject constructor(val appState: AppStateManager, val createUserInteractor: CreateUserInteractor) :
        LoginComponentContract.Presenter,
        BasePresenterImpl<LoginComponentContract.View>(),
        CreateUserInteractor.Output
{

    init {
        createUserInteractor.output = this
    }

    override fun createUser(email: String) {
        val user : User = User(-1, email, email, null)
        createUserInteractor.input = CreateUserInteractor.Input(user)
        createUserInteractor.run()
    }

    override fun onCreateUser(user: User) {
        Timber.e("User created $user")
    }

    override fun onCreateUserError(msg: String) {
        Timber.e(msg)
    }
}