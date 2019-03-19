package dk.eboks.app.mail.presentation.ui.message.screens.reply

import dk.eboks.app.domain.models.formreply.FormInput
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface ReplyFormContract {
    interface View : BaseView {
        fun showProgress(show: Boolean)
        fun clearForm()
        fun showFormInput(input: FormInput)
        fun finish()
    }

    interface Presenter : BasePresenter<View> {
        fun setup(msg: Message)
        fun submit(formList: List<FormInput>)
    }
}