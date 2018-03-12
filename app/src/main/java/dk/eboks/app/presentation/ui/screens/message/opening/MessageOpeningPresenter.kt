package dk.eboks.app.presentation.ui.screens.message.opening

import dk.eboks.app.domain.interactors.ServerErrorHandler
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.protocol.ServerError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber

/**
 * Created by bison on 20-05-2017.
 */
class MessageOpeningPresenter(val appStateManager: AppStateManager, val executor: Executor) : MessageOpeningContract.Presenter, BasePresenterImpl<MessageOpeningContract.View>() {
    val serverError : ServerError? = appStateManager.state?.openingState?.serverError

    init {
        serverError?.let { error ->
            processError(error)
        }.guard { Timber.e("No ServerError found") }
    }

    fun processError(error: ServerError)
    {
        when(error.code)
        {
            ServerErrorHandler.NO_PRIVATE_SENDER_WARNING -> runAction { v-> v.setPrivateSenderWarningFragment() }
        }
    }

    override fun signalMessageOpenDone() {
        executor.signal("messageOpenDone")
    }
}