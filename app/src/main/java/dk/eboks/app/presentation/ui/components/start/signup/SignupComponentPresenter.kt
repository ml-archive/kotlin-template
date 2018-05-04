package dk.eboks.app.presentation.ui.components.start.signup

import dk.eboks.app.domain.interactors.user.CreateUserInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class SignupComponentPresenter @Inject constructor(
        val appState: AppStateManager,
        val createUserInteractor: CreateUserInteractor
) :
        SignupComponentContract.Presenter,
        BasePresenterImpl<SignupComponentContract.SignupView>(),
        CreateUserInteractor.Output {

    init {
        createUserInteractor.output = this
    }

    override fun setName(name: String) {
        tempUser.name = name
    }

    override fun setEmail(email: String) {
        tempUser.setPrimaryEmail(email)
    }

    override fun setPassword(password: String) {

    }

    override fun createUserAndLogin() {
        tempUser.lastLoginProvider = "email"
        appState.state?.currentUser = tempUser
        appState.save()
        createUserInteractor.input = CreateUserInteractor.Input(tempUser)
        createUserInteractor.run()
    }

    override fun onCreateUser(user: User, numberOfUsers: Int) {

    }

    override fun onCreateUserError(error: ViewError) {
        runAction { v -> v.showErrorDialog(error) }
    }

    companion object {
        val tempUser: User = User()
    }
}