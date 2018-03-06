package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.exceptions.InteractorException
import dk.eboks.app.domain.exceptions.ServerErrorException
import dk.eboks.app.domain.interactors.ServerErrorHandler
import dk.eboks.app.domain.managers.*
import dk.eboks.app.domain.models.Message
import dk.eboks.app.domain.models.ServerError
import dk.eboks.app.domain.models.internal.EboksContentType
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.util.FieldMapper
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

    private var errorHandler = ServerErrorHandler(uiManager, executor, appStateManager)

    override fun execute() {
        try {
            input?.msg?.let { msg->
                // TODO the result of this call can result in all sorts of fun control flow changes depending on what error code the backend returns
                val updated_msg = messagesRepository.getMessage(msg.folder?.id ?: 0, msg.id)
                // update the (perhaps) more detailed message object with the extra info from the backend
                // because the JVM can only deal with reference types silly reflection tricks like this are necessary
                FieldMapper.copyAllFields(msg, updated_msg)
                openMessage(msg)
            }
        }
        catch (e: Throwable) {
            e.printStackTrace()
            if(e is ServerErrorException)
            {
                val outcome = errorHandler.handle(e.error)
                if(outcome == ServerErrorHandler.REPEAT)
                {
                    when(e.error.code)
                    {
                        ServerErrorHandler.NO_PRIVATE_SENDER_WARNING -> {
                            input?.msg?.let { msg->
                                try {
                                    val updated_msg = messagesRepository.getMessage(input?.msg?.folder?.id ?: 0, input?.msg?.id ?: "", null, true)
                                    FieldMapper.copyAllFields(msg, updated_msg)
                                    openMessage(msg)
                                }
                                catch (t : Throwable) {output?.onOpenMessageError("Unknown error opening message ${e.message}")}
                            }.guard { output?.onOpenMessageError("Unknown error opening message ${e.message}") }
                        }
                        else -> runOnUIThread { output?.onOpenMessageDone() }
                    }
                }
                else {
                    runOnUIThread {
                        runOnUIThread { output?.onOpenMessageDone() }
                    }
                }
                return
            }
            runOnUIThread {
                output?.onOpenMessageError("Unknown error opening message ${e.message}")
            }
        }
    }

    fun openMessage(msg : Message)
    {
        msg.content?.let { content->
            var filename = cacheManager.getCachedContentFileName(content)
            if(filename == null) // is not in users
            {
                Timber.e("Content ${content.id} not in users, downloading")
                // TODO the result of this call can result in all sorts of fun control flow changes depending on what error code the backend returns
                filename = downloadManager.downloadAttachmentContent(msg, content)
                if(filename == null)
                    throw(InteractorException("Could not download content ${content.id}"))
                Timber.e("Downloaded content to $filename")
                cacheManager.cacheContent(filename, content)
            }
            else
            {
                Timber.e("Found content in users ($filename)")
            }

            appStateManager.state?.currentMessage = msg
            appStateManager.state?.currentViewerFileName = cacheManager.getAbsolutePath(filename)
            appStateManager.save()

            if(isEmbeddedType(msg))
            {
                uiManager.showEmbeddedMessageScreen()
            }
            else {
                uiManager.showMessageScreen()
            }
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
                EboksContentType("pdf", "application/pdf"),
                EboksContentType("png", "image/png"),
                EboksContentType("jpg", "image/jpeg"),
                EboksContentType("jpeg", "image/jpeg"),
                EboksContentType("gif", "image/gif"),
                EboksContentType("bmp", "image/bmp"),
                EboksContentType("html", "text/html"),
                EboksContentType("htm", "text/html"),
                EboksContentType("txt", "text/plain")
        )
    }
}