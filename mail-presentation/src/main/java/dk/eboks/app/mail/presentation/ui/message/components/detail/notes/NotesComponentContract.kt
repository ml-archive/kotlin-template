package dk.eboks.app.mail.presentation.ui.message.components.detail.notes

import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface NotesComponentContract {
    interface View : BaseView {
        fun updateView(message: Message)
    }

    interface Presenter : BasePresenter<View> {
        fun setup()
        fun saveNote(note: String)
    }
}