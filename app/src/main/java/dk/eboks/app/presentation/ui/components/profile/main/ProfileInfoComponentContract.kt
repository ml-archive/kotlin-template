package dk.eboks.app.presentation.ui.components.profile.main

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

interface ProfileInfoComponentContract {
    interface View : BaseView {
        fun setName(name: String)
        fun setProfileImage(url: String?)
        fun setVerified(verified : Boolean)
        fun setFingerprintEnabled(enabled : Boolean, lastProviderId : String? = null)
        fun setKeepMeSignedIn(enabled : Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun loadUserData()
        fun doLogout()
    }
}