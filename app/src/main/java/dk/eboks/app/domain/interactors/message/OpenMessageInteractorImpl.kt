package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.exceptions.InteractorException
import dk.eboks.app.domain.exceptions.ServerErrorException
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.DownloadManager
import dk.eboks.app.domain.managers.FileCacheManager
import dk.eboks.app.domain.managers.UIManager
import dk.eboks.app.domain.models.APIConstants
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.EboksContentType
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.protocol.ErrorType
import dk.eboks.app.domain.models.protocol.ServerError
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.util.FieldMapper
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
 * Created by bison on 01/02/18.
 */
class OpenMessageInteractorImpl(executor: Executor, val appStateManager: AppStateManager,
                                val uiManager: UIManager, val downloadManager: DownloadManager,
                                val cacheManager: FileCacheManager, val messagesRepository: MessagesRepository)
    : BaseInteractor(executor), OpenMessageInteractor {

    override var output: OpenMessageInteractor.Output? = null
    override var input: OpenMessageInteractor.Input? = null

    override fun execute() {
        try {
            input?.msg?.let { msg->
                val updated_msg = messagesRepository.getMessage(msg.folderId, msg.id)
                // update the (perhaps) more detailed message object with the extra info from the backend
                // because the JVM can only deal with reference types silly reflection tricks like this are necessary
                FieldMapper.copyAllFields(msg, updated_msg)

                if(!checkMessageLockState(msg))
                    openMessage(msg)
            }
        }
        catch (t: Throwable) {
            t.printStackTrace()
            if(t is ServerErrorException) {
                input?.msg?.let { handleServerException(t, it) }.guard { output?.onOpenMessageError(exceptionToViewError(t, shouldClose = true)) }
            }
            else runOnUIThread {
                output?.onOpenMessageError(exceptionToViewError(t, shouldClose = true))
            }
        }
    }

    /*
        Handles the server error, returns true if the interactor
        should continue with whatever it was doing, otherwise
        it returns false to indicate the interactor should abort
     */
    fun handle(error : ServerError) : Int
    {
        Timber.e("Handling server error $error")
        // this is probably slow as we allocate a collection just to do a check :)
        if(setOf(
                        NO_PRIVATE_SENDER_WARNING,
                        MANDATORY_OPEN_RECEIPT,
                        VOLUNTARY_OPEN_RECEIPT,
                        MESSAGE_QUARANTINED,
                        MESSAGE_RECALLED,
                        MESSAGE_LOCKED,
                        PROMULGATION).contains(error.code))
        {
            appStateManager.state?.let { state->
                state.openingState.shouldProceedWithOpening = false
                state.openingState.serverError = error
            }

            runOnUIThread {
                output?.onOpenMessageServerError(error)
            }
            //uiManager.showMessageOpeningScreen()
            executor.sleepUntilSignalled("messageOpenDone")
            if(appStateManager.state?.openingState?.shouldProceedWithOpening ?: false)
                return PROCEED
            else
                return ABORT
        }
        else
            return SHOW_ERROR
    }

    fun handleServerException(e : ServerErrorException, msg : Message)
    {
        Timber.e("ServerException arose from getMessage api call")

        val outcome = handle(e.error)

        when(outcome)
        {
            ServerErrorHandler.PROCEED -> {
                try {
                    val updated_msg = messagesRepository.getMessage(input?.msg?.folder?.id ?: 0, input?.msg?.id ?: "", null, true)
                    FieldMapper.copyAllFields(msg, updated_msg)
                    openMessage(msg)
                }
                catch (t : Throwable)
                {
                    runOnUIThread { output?.onOpenMessageError(exceptionToViewError(t, shouldClose = true)) }
                }
            }
            ServerErrorHandler.SHOW_ERROR -> {
                runOnUIThread { output?.onOpenMessageError(exceptionToViewError(e, shouldClose = true)) }
            }
            else -> {
                val ve = ViewError(shouldCloseView = true, shouldDisplay = true)
                runOnUIThread { output?.onOpenMessageError(ve) }
            }
        }

    }

    fun checkMessageLockState(msg : Message) : Boolean
    {
        // check for stupid message protection / locking
        msg.lockStatus?.let { status->
            when(status.type)
            {
                APIConstants.MSG_LOCKED_REQUIRES_HIGHER_IDP_LVL ->
                {

                }
                APIConstants.MSG_LOCKED_REQUIRES_HIGHER_SEC_LVL ->
                {

                }
                APIConstants.MSG_LOCKED_REQUIRES_OTHER_IDP ->
                {

                }
                APIConstants.MSG_LOCKED_UNTIL_ACCEPTED ->
                {

                }
                APIConstants.MSG_LOCKED_WEB_ONLY ->
                {

                }
                else ->
                {
                    return false
                }
            }
        }
        return false
    }

    fun openMessage(msg : Message)
    {
        msg.content?.let { content->
            var filename = cacheManager.getCachedContentFileName(content)
            if(filename == null) // is not in users
            {
                Timber.e("Content ${content.id} not in cache, downloading")
                // TODO the result of this call can result in all sorts of fun control flow changes depending on what error code the backend returns
                filename = downloadManager.downloadContent(msg, content)
                if(filename == null)
                    throw(InteractorException("Could not download content ${content.id}"))
                Timber.e("Downloaded content to $filename")
                cacheManager.cacheContent(filename, content)
            }
            else
            {
                Timber.e("Found content in cache ($filename)")
            }

            appStateManager.state?.currentMessage = msg
            appStateManager.state?.currentViewerFileName = cacheManager.getAbsolutePath(filename)
            //appStateManager.save()

            // abort opening if view is no longer attached
            output?.let {
                if(!it.isViewAttached()) {
                    Timber.e("User dismissed the view, abort opening")
                    return
                }
                else
                {
                    Timber.e("View is still attached, proceeding")
                }
            }
            // set message to unread
            msg.unread = false
            if(isEmbeddedType(msg))
            {
                uiManager.showEmbeddedMessageScreen()
            }
            else {
                uiManager.showMessageScreen()
            }
            //Thread.sleep(500)
            runOnUIThread {
                output?.onOpenMessageDone()
            }
        }
    }

    fun isEmbeddedType(msg : Message) : Boolean
    {
        if(msg.content == null)
            return false
        var ext = msg.content?.fileExtension
        var mime = msg.content?.mimeType
        for(type in embeddedTypes)
        {
            // do we have a mime type? those are the bestest!!
            if(mime != null)
            {
                if(type.mimeType == mime) // recognized
                    return true
            }
            else if(ext != null) // narp go with the oldschool windows file extension
            {
                if(type.fileExtension == ext) {
                    msg.content?.let { it.mimeType = type.mimeType } // enrich with the mimetype if we only have file ext
                    return true
                }
            }
        }
        return false
    }

    companion object {
        var embeddedTypes = listOf<EboksContentType>(
                /* EboksContentType("pdf", "application/pdf"), */
                EboksContentType("png", "image/png"),
                EboksContentType("jpg", "image/jpeg"),
                EboksContentType("jpeg", "image/jpeg"),
                EboksContentType("gif", "image/gif"),
                EboksContentType("bmp", "image/bmp"),
                EboksContentType("html", "text/html"),
                EboksContentType("htm", "text/html"),
                EboksContentType("txt", "text/plain")
        )
        
        val NO_PRIVATE_SENDER_WARNING = 9100
        val MANDATORY_OPEN_RECEIPT = 9200
        val VOLUNTARY_OPEN_RECEIPT = 9201
        val MESSAGE_QUARANTINED = 9300
        val MESSAGE_RECALLED = 9301
        val MESSAGE_LOCKED = 9302
        val PROMULGATION = 12260

        val PROCEED = 1
        val ABORT = 2
        val SHOW_ERROR = 3
    }
}