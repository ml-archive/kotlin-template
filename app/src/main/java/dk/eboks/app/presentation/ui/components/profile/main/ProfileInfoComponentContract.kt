package dk.eboks.app.presentation.ui.components.profile.main

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

interface ProfileInfoComponentContract {
    interface View : BaseView {
        fun setName(name: String)
        fun setProfileImage(url: String?)
        fun setVerified(isVerified: Boolean)
        fun showFingerprintEnabled(isEnabled: Boolean, lastProviderId: String? = null)
        fun showKeepMeSignedIn(isEnabled: Boolean)
        fun logout()
        fun setupListeners()
        fun showProgress(show : Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun loadUserData()
        fun enableUserFingerprint(enable: Boolean)
        fun enableKeepMeSignedIn(enable: Boolean)
        fun doLogout()
        fun saveUserImg(uri: String)
    }
}