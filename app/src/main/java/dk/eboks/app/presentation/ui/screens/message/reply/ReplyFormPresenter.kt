package dk.eboks.app.presentation.ui.screens.message.reply

import dk.eboks.app.domain.interactors.message.GetReplyFormInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.formreply.ReplyForm
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber

/**
 * Created by bison on 20-05-2017.
 */
class ReplyFormPresenter(val appStateManager: AppStateManager, val getReplyFormInteractor: GetReplyFormInteractor) :
        ReplyFormContract.Presenter,
        BasePresenterImpl<ReplyFormContract.View>(),
        GetReplyFormInteractor.Output
{
    init {
        getReplyFormInteractor.output = this
    }

    override fun setup(msg: Message) {
        Timber.e("Setting up reply form for message $msg")
        getReplyFormInteractor.input = GetReplyFormInteractor.Input(msg.id, msg.folder?.id ?:0)
        getReplyFormInteractor.run()
    }

    override fun onGetReplyForm(form: ReplyForm) {
        runAction { v->
            v.showProgress(false)
            for(input in form.inputs)
            {
                v.showFormInput(input)
            }
        }
        Timber.e("Received replyform $form")
    }

    override fun onGetReplyFormError(error: ViewError) {
        runAction { v->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }
}