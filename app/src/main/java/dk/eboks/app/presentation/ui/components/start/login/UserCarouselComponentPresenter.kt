package dk.eboks.app.presentation.ui.components.start.login

import dk.eboks.app.domain.interactors.user.DeleteUserInteractor
import dk.eboks.app.domain.interactors.user.GetUsersInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class UserCarouselComponentPresenter @Inject constructor(val appState: AppStateManager, val getUsersInteractor: GetUsersInteractor, val deleteUserInteractor: DeleteUserInteractor) :
        UserCarouselComponentContract.Presenter,
        BasePresenterImpl<UserCarouselComponentContract.View>(),
        GetUsersInteractor.Output,
        DeleteUserInteractor.Output
{
    init {
        getUsersInteractor.output = this
        deleteUserInteractor.output = this
    }

    override fun requestUsers() {
        getUsersInteractor.run()
    }

    override fun login(user: User) {
        appState.state?.loginState?.selectedUser = user
        runAction { v-> v.openLogin() }
    }

    override fun addAnotherUser() {
        appState.state?.loginState?.selectedUser = null
        runAction { v-> v.openLogin() }
    }

    override fun deleteUser(user: User) {
        deleteUserInteractor.input = DeleteUserInteractor.Input(user)
        deleteUserInteractor.run()
    }

    override fun onGetUsers(users: MutableList<User>) {
        runAction { v-> v.showUsers(users) }
    }

    override fun onGetUsersError(msg: String) {
        Timber.e(msg)
    }

    override fun onDeleteUser(user: User) {
        getUsersInteractor.run()
    }

    override fun onDeleteUserError(msg: String) {
        Timber.e(msg)
    }
}