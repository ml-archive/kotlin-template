package dk.eboks.app.presentation.ui.profile.components.myinfo

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface MyInfoComponentContract {
    interface View : BaseView {
        fun setName(name : String)
        fun setPrimaryEmail(email : String)
        fun setSecondaryEmail(email : String)
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
    }

    interface Presenter : BasePresenter<View> {
        fun setup()
        fun save()
    }
}