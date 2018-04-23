package dk.eboks.app.presentation.ui.screens.message.opening

import dk.eboks.app.domain.interactors.message.OpenMessageInteractor
import dk.eboks.app.domain.interactors.message.ServerErrorHandler
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.protocol.ServerError
import dk.eboks.app.presentation.ui.components.message.opening.privatesender.PrivateSenderWarningComponentFragment
import dk.eboks.app.presentation.ui.components.message.opening.promulgation.PromulgationComponentFragment
import dk.eboks.app.presentation.ui.components.message.opening.protectedmessage.ProtectedMessageComponentFragment
import dk.eboks.app.presentation.ui.components.message.opening.quarantine.QuarantineComponentFragment
import dk.eboks.app.presentation.ui.components.message.opening.recalled.RecalledComponentFragment
import dk.eboks.app.presentation.ui.components.message.opening.receipt.OpeningReceiptComponentFragment
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber

/**
 * Created by bison on 20-05-2017.
 */
class MessageOpeningPresenter(val appStateManager: AppStateManager, val executor: Executor, val openMessageInteractor: OpenMessageInteractor) :
        MessageOpeningContract.Presenter,
        BasePresenterImpl<MessageOpeningContract.View>(),
        OpenMessageInteractor.Output
{
    //val serverError : ServerError? = appStateManager.state?.openingState?.serverError

    init {
        openMessageInteractor.output = this
        /*
        serverError?.let { error ->
            processError(error)
        }.guard { Timber.e("No ServerError found") }
        */
    }

    override fun setup(msg: Message) {
        openMessageInteractor.input = OpenMessageInteractor.Input(msg)
        openMessageInteractor.run()
    }

    override fun signalMessageOpenDone() {
        executor.signal("messageOpenDone")
    }

    override fun onOpenMessageDone() {
        runAction { v->v.finish() }
    }

    override fun onOpenMessageError(error: ViewError) {
        runAction { v->v.showErrorDialog(error) }
    }

    override fun onOpenMessageServerError(error: ServerError) {
        when(error.code)
        {
            ServerErrorHandler.NO_PRIVATE_SENDER_WARNING -> runAction { v-> v.setOpeningFragment(PrivateSenderWarningComponentFragment::class.java) }
            ServerErrorHandler.MESSAGE_LOCKED -> runAction { v-> v.setOpeningFragment(ProtectedMessageComponentFragment::class.java) }
            ServerErrorHandler.MANDATORY_OPEN_RECEIPT -> runAction { v-> v.setOpeningFragment(OpeningReceiptComponentFragment::class.java) }
            ServerErrorHandler.VOLUNTARY_OPEN_RECEIPT -> runAction { v-> v.setOpeningFragment(OpeningReceiptComponentFragment::class.java) }
            ServerErrorHandler.MESSAGE_QUARANTINED -> runAction { v-> v.setOpeningFragment(QuarantineComponentFragment::class.java) }
            ServerErrorHandler.MESSAGE_RECALLED -> runAction { v-> v.setOpeningFragment(RecalledComponentFragment::class.java) }
            ServerErrorHandler.PROMULGATION -> runAction { v-> v.setOpeningFragment(PromulgationComponentFragment::class.java) }
        }
    }
}