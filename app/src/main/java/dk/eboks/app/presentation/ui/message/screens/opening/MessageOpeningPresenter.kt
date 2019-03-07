package dk.eboks.app.presentation.ui.message.screens.opening

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.protocol.ServerError
import dk.eboks.app.mail.domain.interactors.message.OpenMessageInteractor
import dk.eboks.app.presentation.ui.message.components.opening.privatesender.PrivateSenderWarningComponentFragment
import dk.eboks.app.presentation.ui.message.components.opening.promulgation.PromulgationComponentFragment
import dk.eboks.app.presentation.ui.message.components.opening.protectedmessage.ProtectedMessageComponentFragment
import dk.eboks.app.presentation.ui.message.components.opening.quarantine.QuarantineComponentFragment
import dk.eboks.app.presentation.ui.message.components.opening.recalled.RecalledComponentFragment
import dk.eboks.app.presentation.ui.message.components.opening.receipt.OpeningReceiptComponentFragment
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class MessageOpeningPresenter @Inject constructor(
    private val executor: Executor,
    private val openMessageInteractor: OpenMessageInteractor
) :
    MessageOpeningContract.Presenter,
    BasePresenterImpl<MessageOpeningContract.View>(),
    OpenMessageInteractor.Output {
    // val serverError : ServerError? = appStateManager.state?.openingState?.serverError

    private var lockedMessage: Message? = null

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
        view { finish() }
    }

    override fun onOpenMessageError(error: ViewError) {
        view { showErrorDialog(error) }
    }

    override fun onOpenMessageServerError(serverError: ServerError) {
        when (serverError.code) {
            OpenMessageInteractor.NO_PRIVATE_SENDER_WARNING -> view {
                setOpeningFragment(
                    PrivateSenderWarningComponentFragment::class.java
                )
            }
            OpenMessageInteractor.MESSAGE_LOCKED -> view {
                setOpeningFragment(
                    ProtectedMessageComponentFragment::class.java
                )
            }
            OpenMessageInteractor.MANDATORY_OPEN_RECEIPT -> view {
                setOpeningFragment(
                    OpeningReceiptComponentFragment::class.java,
                    voluntaryReceipt = false
                )
            }
            OpenMessageInteractor.VOLUNTARY_OPEN_RECEIPT -> view {
                setOpeningFragment(
                    OpeningReceiptComponentFragment::class.java,
                    voluntaryReceipt = true
                )
            }
            OpenMessageInteractor.MESSAGE_QUARANTINED -> view {
                setOpeningFragment(
                    QuarantineComponentFragment::class.java
                )
            }
            OpenMessageInteractor.MESSAGE_RECALLED -> view {
                setOpeningFragment(
                    RecalledComponentFragment::class.java
                )
            }
            OpenMessageInteractor.PROMULGATION -> view {
                setOpeningFragment(
                    PromulgationComponentFragment::class.java
                )
            }
        }
    }

    override fun onReAuthenticate(loginProviderId: String, msg: Message) {
        Timber.e("Must reauthenticate with $loginProviderId")
        lockedMessage = msg
        view { showMessageLocked(loginProviderId, msg) }
    }

    override fun onPrivateSenderWarning(msg: Message) {
        view { setOpeningFragment(PrivateSenderWarningComponentFragment::class.java) }
    }

    override fun isViewAttached(): Boolean {
        return view != null
    }
}