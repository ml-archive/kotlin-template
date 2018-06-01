package dk.eboks.app.presentation.ui.components.profile.main

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter
import java.io.File

interface ProfileInfoComponentContract {
    interface View : BaseView {
        fun setName(name: String)
        fun setProfileImage(url: String?)
        fun setVerified(verified: Boolean)
        fun showFingerprintEnabled(enabled: Boolean, lastProviderId: String? = null)
        fun setKeepMeSignedIn(enabled: Boolean)
        fun logout()
    }

    interface Presenter : BasePresenter<View> {
        fun loadUserData()
        fun enableUserFingerprint(isEnabled: Boolean)
        fun doLogout()
        fun saveUserImg(uri: String)
    }
}