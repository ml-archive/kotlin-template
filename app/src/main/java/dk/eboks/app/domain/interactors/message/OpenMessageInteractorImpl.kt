package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.interactors.InteractorException
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.DownloadManager
import dk.eboks.app.domain.managers.FileCacheManager
import dk.eboks.app.domain.managers.UIManager
import dk.eboks.app.domain.models.Message
import dk.eboks.app.domain.models.internal.EboksContentType
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.util.FieldMapper
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

                // TODO the result of this call can result in all sorts of fun control flow changes depending on what error code the backend returns
                val updated_msg = messagesRepository.getMessage(false, msg.folder?.id ?: 0, msg.id.toString())


                // update the (perhaps) more detailed message object with the extra info from the backend
                FieldMapper.copyAllFields(msg, updated_msg)

                msg.content?.let { content->
                    var filename = cacheManager.getCachedContentFileName(content)
                    if(filename == null) // is not in cache
                    {
                        Timber.e("Content ${content.id} not in cache, downloading")
                        // TODO the result of this call can result in all sorts of fun control flow changes depending on what error code the backend returns
                        filename = downloadManager.downloadContent(content)
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
        } catch (e: Throwable) {
            e.printStackTrace()
            runOnUIThread {
                output?.onOpenMessageError("Unknown error opening message ${input?.msg}")
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