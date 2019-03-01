package dk.eboks.app.keychain.presentation.components

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.keychain.interactors.user.DeleteUserInteractor
import dk.eboks.app.keychain.interactors.user.GetUsersInteractor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class UserCarouselComponentPresenter @Inject constructor(
    private val appState: AppStateManager,
    private val userSettingsManager: UserSettingsManager,
    private val getUsersInteractor: GetUsersInteractor,
    private val deleteUserInteractor: DeleteUserInteractor
) :
    UserCarouselComponentContract.Presenter,
    BasePresenterImpl<UserCarouselComponentContract.View>(),
    GetUsersInteractor.Output,
    DeleteUserInteractor.Output {
    init {
        getUsersInteractor.output = this
        deleteUserInteractor.output = this
    }

    override fun requestUsers() {
        getUsersInteractor.run()
    }

    override fun login(user: User) {
        appState.state?.loginState?.selectedUser = user
        runAction { v -> v.openLogin() }
    }

    override fun addAnotherUser() {
        appState.state?.loginState?.selectedUser = null
        runAction { v -> v.openLogin() }
    }

    override fun clearSelectedUser() {
        appState.state?.loginState?.let { state ->
            state.selectedUser = null
            state.lastUser = null
            state.firstLogin = false
            appState.save()
        }
    }

    override fun deleteUser(user: User) {
        deleteUserInteractor.input = DeleteUserInteractor.Input(user)
        deleteUserInteractor.run()
    }

    override fun onGetUsers(users: MutableList<User>) {
        val list = MutableList(
            users.size,
            init = { Pair(users[it], userSettingsManager[users[it].id]) })

        runAction { v ->
            v.showUsers(list)
            appState.state?.loginState?.lastUser?.let { user ->
                Timber.e("Setting selected user $user")
                v.setSelectedUser(user)
            }
        }
    }

    override fun onGetUsersError(error: ViewError) {
        runAction { it.showErrorDialog(error) }
    }

    override fun onDeleteUser(user: User) {
        getUsersInteractor.run()
    }

    override fun onDeleteUserError(error: ViewError) {
        runAction { it.showErrorDialog(error) }
    }
}