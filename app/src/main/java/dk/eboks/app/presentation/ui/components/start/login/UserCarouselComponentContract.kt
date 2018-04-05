package dk.eboks.app.presentation.ui.components.start.login

import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface UserCarouselComponentContract {
    interface View : BaseView {
        fun showUsers(users : MutableList<User>)
        fun setSelectedUser(user : User)
        fun openLogin()
    }

    interface Presenter : BasePresenter<View> {
        fun requestUsers()
        fun login(user: User)
        fun addAnotherUser()
        fun deleteUser(user : User)
        fun clearSelectedUser()
    }
}