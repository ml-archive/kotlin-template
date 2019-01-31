package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.config.Config
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
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.util.FieldMapper
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber
import java.io.File

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
                //throw(ServerErrorException(ServerError(id="homemade", code = PROMULGATION, type = ERROR)))
                val updated_msg = messagesRepository.getMessage(msg.folderId, msg.id, acceptedPrivateTerms = appStateManager.state?.openingState?.acceptPrivateTerms)
                Timber.d("Opening meessage with lock status: ${msg.lockStatus}")
                if(processLockedMessage(msg)) {
                    // update the (perhaps) more detailed message object with the extra info from the backend
                    // because the JVM can only deal with reference types silly reflection tricks like this are necessary
                    FieldMapper.copyAllFields(msg, updated_msg)
                    openMessage(msg, acceptedPrivateTerms = appStateManager.state?.openingState?.acceptPrivateTerms ?: false)
                }
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
        Handle special opening conditions. This utilizes a mutex to make the interacter thread (one running this) sleep while the UI
        is displaying opening screens. If you don't know what mean, you're not allowed to ride this attraction son
     */
    fun handleServerException(e : ServerErrorException, msg : Message) {
        Timber.e("ServerException arose from getMessage api call")
        when (e.error.code) {
            MANDATORY_OPEN_RECEIPT -> {
                appStateManager.state?.let { state ->
                    state.openingState.shouldProceedWithOpening = false
                    state.openingState.serverError = e.error
                }

                runOnUIThread {
                    output?.onOpenMessageServerError(e.error)
                }

                // goto sleep while the UI finishes it thang
                executor.sleepUntilSignalled("messageOpenDone")
                if (appStateManager.state?.openingState?.shouldProceedWithOpening == true) {
                    try {
                        if(processLockedMessage(msg)) {
                            val updated_msg = messagesRepository.getMessage(input?.msg?.folder?.id
                                    ?: input?.msg?.folderId ?: 0, input?.msg?.id
                                    ?: "", receipt = true, acceptedPrivateTerms = appStateManager.state?.openingState?.acceptPrivateTerms)
                            FieldMapper.copyAllFields(msg, updated_msg)
                            openMessage(msg, true)
                        }
                    } catch (t: Throwable) {
                        runOnUIThread { output?.onOpenMessageError(exceptionToViewError(t, shouldClose = true)) }
                    }
                } else    // abort
                {
                    val ve = ViewError(shouldCloseView = true, shouldDisplay = false)
                    runOnUIThread { output?.onOpenMessageError(ve) }
                }
            }
            VOLUNTARY_OPEN_RECEIPT -> {
                appStateManager.state?.let { state ->
                    state.openingState.shouldProceedWithOpening = false
                    state.openingState.serverError = e.error
                }

                runOnUIThread {
                    output?.onOpenMessageServerError(e.error)
                }

                // goto sleep while the UI finishes it thang
                executor.sleepUntilSignalled("messageOpenDone")
                if (appStateManager.state?.openingState?.shouldProceedWithOpening == true) {
                    try {
                        if(processLockedMessage(msg)) {
                            val updated_msg = messagesRepository.getMessage(input?.msg?.folder?.id
                                    ?: input?.msg?.folderId ?: 0, input?.msg?.id
                                    ?: "", receipt = appStateManager.state?.openingState?.sendReceipt
                                    ?: false, acceptedPrivateTerms = appStateManager.state?.openingState?.acceptPrivateTerms)
                            FieldMapper.copyAllFields(msg, updated_msg)
                            openMessage(msg, true)
                        }
                    } catch (t: Throwable) {
                        runOnUIThread { output?.onOpenMessageError(exceptionToViewError(t, shouldClose = true)) }
                    }
                } else    // abort
                {
                    val ve = ViewError(shouldCloseView = true, shouldDisplay = false)
                    runOnUIThread { output?.onOpenMessageError(ve) }
                }
            }
            MESSAGE_QUARANTINED -> {
            }
            MESSAGE_RECALLED -> {
            }
            MESSAGE_LOCKED -> {
            }
            PROMULGATION -> {
                appStateManager.state?.let { state ->
                    state.openingState.shouldProceedWithOpening = false
                    state.openingState.serverError = e.error
                }

                runOnUIThread {
                    output?.onOpenMessageServerError(e.error)
                }

                // goto sleep while the UI finishes it thang
                executor.sleepUntilSignalled("messageOpenDone")
                if (appStateManager.state?.openingState?.shouldProceedWithOpening == true) {
                    try {
                        if(processLockedMessage(msg)) {
                            val updated_msg = messagesRepository.getMessage(input?.msg?.folder?.id
                                    ?: input?.msg?.folderId ?: 0, input?.msg?.id ?: "", receipt = null, acceptedPrivateTerms = appStateManager.state?.openingState?.acceptPrivateTerms)
                            FieldMapper.copyAllFields(msg, updated_msg)
                            openMessage(msg, true)
                        }
                    } catch (t: Throwable) {
                        runOnUIThread { output?.onOpenMessageError(exceptionToViewError(t, shouldClose = true)) }
                    }
                } else    // abort
                {
                    val ve = ViewError(shouldCloseView = true, shouldDisplay = false)
                    runOnUIThread { output?.onOpenMessageError(ve) }
                }
            }
            else -> {
                runOnUIThread { output?.onOpenMessageError(exceptionToViewError(e, shouldClose = true)) }
            }
        }
    }

    /*
        TODO needs support for norway and sweden

        return true if message opening should continue at some later point or false
        to do nothing
     */
    private fun processLockedMessage(msg : Message) : Boolean
    {
        // TODO for the love of god, memba to 'move me (for testing locked messages status 1)
        //msg.lockStatus?.type = 5
        // check for stupid message protection / locking
        msg.lockStatus?.let { status->
            when(status.type)
            {
                APIConstants.MSG_LOCKED_REQUIRES_NEW_AUTH ->
                {
                    runOnUIThread { output?.onReAuthenticate("cpr", msg) }
                    executor.sleepUntilSignalled("authenticationDone")
                    return true
                }
                APIConstants.MSG_LOCKED_REQUIRES_HIGHER_SEC_LVL ->
                {
                    Config.getVerificationProviderId()?.let { providerId ->
                        runOnUIThread { output?.onReAuthenticate(providerId, msg) }
                        executor.sleepUntilSignalled("authenticationDone")
                        return true
                    }
                }
                APIConstants.MSG_LOCKED_WEB_ONLY ->
                {
                    runOnUIThread { output?.onReAuthenticate("webonly", msg) }
                    executor.sleepUntilSignalled("authenticationDone")
                    return false
                }
                APIConstants.MSG_LOCKED_REQUIRES_PUBLIC_IDP2 ->
                {
                    runOnUIThread { output?.onPrivateSenderWarning(msg) }
                    executor.sleepUntilSignalled("messageOpenDone")
                    if(appStateManager.state?.openingState?.shouldProceedWithOpening == true)
                    {
                        appStateManager.state?.openingState?.acceptPrivateTerms = true
                        //openMessage(currentmsg = msg, acceptedPrivateTerms = true)
                        return true
                    }
                    else
                        return false
                }

                else ->
                {
                    return true
                }
            }
        }
        return true
    }

    private fun openMessage(currentmsg : Message, secondAttempt : Boolean = false, acceptedPrivateTerms : Boolean = false)
    {
        var msg = currentmsg
        if (acceptedPrivateTerms){
            msg = messagesRepository.getMessage(input?.msg?.folder?.id
                    ?: input?.msg?.folderId ?: 0, input?.msg?.id
                    ?: "", receipt = null, acceptedPrivateTerms = acceptedPrivateTerms)
        }

        msg.content?.let { content->
            var filename = cacheManager.getCachedContentFileName(content)
            if(filename == null) // is not in users
            {
                Timber.e("Content ${content.id} not in cache, downloading")
                filename = downloadManager.downloadContent(msg, content)
                if(filename == null)
                    throw(InteractorException("Could not download content ${content.id}"))
                Timber.e("Downloaded content to $filename")
                cacheManager.cacheContent(filename, content)
            }
            else
            {
                Timber.e("Found content in cache ($filename)")
                val f = File(cacheManager.getAbsolutePath(filename))
                if(!f.exists())
                {
                    filename = downloadManager.downloadContent(msg, content)
                    if(filename == null)
                        throw(InteractorException("Could not download content ${content.id}"))
                    Timber.e("Downloaded content to $filename")
                    cacheManager.cacheContent(filename, content)
                }
            }

            appStateManager.state?.currentMessage = msg
            appStateManager.state?.currentViewerFileName = cacheManager.getAbsolutePath(filename)
            //appStateManager.save()

            // abort opening if view is no longer attached
            if(!secondAttempt) {
                output?.let {
                    if (!it.isViewAttached()) {
                        Timber.e("User dismissed the view, abort opening")
                        return
                    } else {
                        Timber.e("View is still attached, proceeding")
                    }
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
                EboksContentType("txt", "text/plain"),
                EboksContentType("pdf", "application/pdf")
        )
        
        val NO_PRIVATE_SENDER_WARNING = 9100
        val MANDATORY_OPEN_RECEIPT = 12194
        val VOLUNTARY_OPEN_RECEIPT = 12245
        val MESSAGE_QUARANTINED = 9300
        val MESSAGE_RECALLED = 9301
        val MESSAGE_LOCKED = 9302
        val PROMULGATION = 12260
    }
}