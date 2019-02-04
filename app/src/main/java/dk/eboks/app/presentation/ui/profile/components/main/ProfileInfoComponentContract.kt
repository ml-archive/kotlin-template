package dk.eboks.app.presentation.ui.profile.components.main

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

interface ProfileInfoComponentContract {
    interface View : BaseView {
        fun setName(name: String)
        fun setProfileImage(url: String?)
        fun setVerified(isVerified: Boolean)
        fun showFingerprintEnabled(isEnabled: Boolean, lastProviderId: String? = null)
        fun showFingerprintOptionIfSupported()
        fun showKeepMeSignedIn(isEnabled: Boolean)
        fun logout()
        fun showProgress(show: Boolean)
        fun attachListeners()
        fun detachListeners()
    }

    interface Presenter : BasePresenter<View> {
        fun loadUserData(showProgress: Boolean)
        fun enableUserFingerprint(enable: Boolean)
        fun enableKeepMeSignedIn(enable: Boolean)
        fun doLogout()
        fun saveUserImg(uri: String)
        var isUserVerified: Boolean
    }
}