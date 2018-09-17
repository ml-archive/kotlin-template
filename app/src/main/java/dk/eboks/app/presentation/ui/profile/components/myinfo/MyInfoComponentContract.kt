package dk.eboks.app.presentation.ui.profile.components.myinfo

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface MyInfoComponentContract {
    interface View : BaseView {
        fun setName(name : String)
        fun setPrimaryEmail(email : String, verified:Boolean, userVerified : Boolean)
        fun setSecondaryEmail(email : String, verified:Boolean)
        fun setMobileNumber(mobile : String, verified:Boolean)
        fun setNewsletter(b : Boolean)
        fun getName() : String
        fun getPrimaryEmail() : String
        fun getSecondaryEmail() : String
        fun getMobileNumber() : String
        fun getNewsletter() : Boolean
        fun showProgress(show : Boolean)
        fun setSaveEnabled(enabled : Boolean)
        fun setNeutralFocus()
        fun onDone()
        //fun showPrimaryEmail(show : Boolean)
        fun showSecondaryEmail(show : Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun setup()
        fun save(closeView : Boolean = true)
        fun refresh()
    }
}