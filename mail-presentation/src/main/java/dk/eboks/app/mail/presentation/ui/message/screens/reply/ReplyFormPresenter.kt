package dk.eboks.app.mail.presentation.ui.message.screens.reply

import dk.eboks.app.mail.domain.interactors.message.GetReplyFormInteractor
import dk.eboks.app.mail.domain.interactors.message.SubmitReplyFormInteractor
import dk.eboks.app.domain.models.formreply.ReplyForm
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class ReplyFormPresenter @Inject constructor(
    private val getReplyFormInteractor: GetReplyFormInteractor,
    private val submitReplyFormInteractor: SubmitReplyFormInteractor
) :
    ReplyFormContract.Presenter,
    BasePresenterImpl<ReplyFormContract.View>(),
    GetReplyFormInteractor.Output,
    SubmitReplyFormInteractor.Output {
    private var currentForm: ReplyForm? = null
    private var currentMessage: Message? = null

    init {
        getReplyFormInteractor.output = this
        submitReplyFormInteractor.output = this
    }

    override fun setup(msg: Message) {
        Timber.e("Setting up reply form for message $msg")
        currentMessage = msg
        getReplyFormInteractor.input = GetReplyFormInteractor.Input(msg.id, msg.folder?.id ?: 0)
        getReplyFormInteractor.run()
        view { clearForm() }
    }

    override fun submit() {
        if (currentMessage != null && currentForm != null) {
            submitReplyFormInteractor.input =
                SubmitReplyFormInteractor.Input(currentMessage!!, currentForm!!)
            submitReplyFormInteractor.run()
        }
    }

    override fun onGetReplyForm(form: ReplyForm) {
        currentForm = form
        view {
            showProgress(false)
            for (input in form.inputs) {
                showFormInput(input)
            }
        }
        Timber.e("Received replyform $form")
    }

    override fun onGetReplyFormError(error: ViewError) {
        view {
            showProgress(false)
            showErrorDialog(error)
        }
    }

    override fun onSubmitReplyForm() {
        view { finish() }
    }

    override fun onSubmitReplyFormError(error: ViewError) {
        view { showErrorDialog(error) }
    }
}