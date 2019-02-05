package dk.eboks.app.presentation.ui.message.components.detail.notes

import dk.eboks.app.domain.interactors.message.messageoperations.UpdateMessageInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.message.MessagePatch
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class NotesComponentPresenter @Inject constructor(
    val appState: AppStateManager,
    val updateMessageInteractor: UpdateMessageInteractor
) :
    NotesComponentContract.Presenter,
    BasePresenterImpl<NotesComponentContract.View>(),
    UpdateMessageInteractor.Output {

    var currentMessage: Message? = appState.state?.currentMessage

    init {
        updateMessageInteractor.output = this
    }

    override fun setup() {
        runAction { v ->
            currentMessage?.let { v.updateView(it) }
        }
    }

    override fun saveNote(note: String) {
        currentMessage?.let { msg ->
            val messages = arrayListOf(msg)
            updateMessageInteractor.input =
                UpdateMessageInteractor.Input(messages, MessagePatch(note = note))
            updateMessageInteractor.run()
        }
    }

    override fun onUpdateMessageSuccess() {
        Timber.e("update success")
    }

    override fun onUpdateMessageError(error: ViewError) {
        runAction { v -> v.showErrorDialog(error) }
    }
}