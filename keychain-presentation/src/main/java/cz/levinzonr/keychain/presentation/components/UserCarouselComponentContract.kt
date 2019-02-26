package cz.levinzonr.keychain.presentation.components

import dk.eboks.app.domain.models.login.User
import dk.eboks.app.domain.models.login.UserSettings
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface UserCarouselComponentContract {
    interface View : BaseView {
        fun showUsers(users: MutableList<Pair<User, UserSettings>>)
        fun setSelectedUser(user: User)
        fun openLogin()
    }

    interface Presenter : BasePresenter<View> {
        fun requestUsers()
        fun login(user: User)
        fun addAnotherUser()
        fun deleteUser(user: User)
        fun clearSelectedUser()
    }
}