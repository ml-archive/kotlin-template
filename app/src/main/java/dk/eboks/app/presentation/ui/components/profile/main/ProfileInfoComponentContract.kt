package dk.eboks.app.presentation.ui.components.profile.main

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

interface ProfileInfoComponentContract {
    interface View : BaseView {
        fun setName(name: String)
        fun setProfileImage(url: String?)
    }

    interface Presenter : BasePresenter<View> {
        fun loadUserData()
        fun doLogout()
    }
}