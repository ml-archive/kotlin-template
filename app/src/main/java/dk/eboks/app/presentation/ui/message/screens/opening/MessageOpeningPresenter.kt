package dk.eboks.app.presentation.ui.message.screens.opening

import dk.eboks.app.domain.interactors.message.OpenMessageInteractor
import dk.eboks.app.domain.interactors.message.OpenMessageInteractorImpl
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.protocol.ServerError
import dk.eboks.app.presentation.ui.message.components.opening.privatesender.PrivateSenderWarningComponentFragment
import dk.eboks.app.presentation.ui.message.components.opening.promulgation.PromulgationComponentFragment
import dk.eboks.app.presentation.ui.message.components.opening.protectedmessage.ProtectedMessageComponentFragment
import dk.eboks.app.presentation.ui.message.components.opening.quarantine.QuarantineComponentFragment
import dk.eboks.app.presentation.ui.message.components.opening.recalled.RecalledComponentFragment
import dk.eboks.app.presentation.ui.message.components.opening.receipt.OpeningReceiptComponentFragment
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber

/**
 * Created by bison on 20-05-2017.
 */
class MessageOpeningPresenter(
    val appStateManager: AppStateManager,
    val executor: Executor,
    val openMessageInteractor: OpenMessageInteractor
) :
    MessageOpeningContract.Presenter,
    BasePresenterImpl<MessageOpeningContract.View>(),
    OpenMessageInteractor.Output {
    // val serverError : ServerError? = appStateManager.state?.openingState?.serverError

    var lockedMessage: Message? = null
    var messageToOpen: Message? = null

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
        runAction { v -> v.finish() }
    }

    override fun onOpenMessageError(error: ViewError) {
        runAction { v -> v.showErrorDialog(error) }
    }

    override fun onOpenMessageServerError(serverError: ServerError) {
        when (serverError.code) {
            OpenMessageInteractorImpl.NO_PRIVATE_SENDER_WARNING -> runAction { v ->
                v.setOpeningFragment(
                    PrivateSenderWarningComponentFragment::class.java
                )
            }
            OpenMessageInteractorImpl.MESSAGE_LOCKED -> runAction { v ->
                v.setOpeningFragment(
                    ProtectedMessageComponentFragment::class.java
                )
            }
            OpenMessageInteractorImpl.MANDATORY_OPEN_RECEIPT -> runAction { v ->
                v.setOpeningFragment(
                    OpeningReceiptComponentFragment::class.java,
                    voluntaryReceipt = false
                )
            }
            OpenMessageInteractorImpl.VOLUNTARY_OPEN_RECEIPT -> runAction { v ->
                v.setOpeningFragment(
                    OpeningReceiptComponentFragment::class.java,
                    voluntaryReceipt = true
                )
            }
            OpenMessageInteractorImpl.MESSAGE_QUARANTINED -> runAction { v ->
                v.setOpeningFragment(
                    QuarantineComponentFragment::class.java
                )
            }
            OpenMessageInteractorImpl.MESSAGE_RECALLED -> runAction { v ->
                v.setOpeningFragment(
                    RecalledComponentFragment::class.java
                )
            }
            OpenMessageInteractorImpl.PROMULGATION -> runAction { v ->
                v.setOpeningFragment(
                    PromulgationComponentFragment::class.java
                )
            }
        }
    }

    override fun onReAuthenticate(loginProviderId: String, msg: Message) {
        Timber.e("Must reauthenticate with $loginProviderId")
        lockedMessage = msg
        runAction { v -> v.showMessageLocked(loginProviderId, msg) }
    }

    override fun onPrivateSenderWarning(msg: Message) {
        runAction { v -> v.setOpeningFragment(PrivateSenderWarningComponentFragment::class.java) }
    }

    override fun isViewAttached(): Boolean {
        return view != null
    }
}