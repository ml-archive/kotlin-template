package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.interactors.InteractorException
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.DownloadManager
import dk.eboks.app.domain.managers.FileCacheManager
import dk.eboks.app.domain.managers.UIManager
import dk.eboks.app.domain.models.Content
import dk.eboks.app.domain.models.internal.EboksContentType
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
 * Created by bison on 01/02/18.
 */
class OpenAttachmentInteractorImpl(executor: Executor, val appStateManager: AppStateManager,
                                   val uiManager: UIManager, val downloadManager: DownloadManager,
                                   val cacheManager: FileCacheManager)
    : BaseInteractor(executor), OpenAttachmentInteractor {

    override var output: OpenAttachmentInteractor.Output? = null
    override var input: OpenAttachmentInteractor.Input? = null

    override fun execute() {
        try {
            input?.attachment?.let { content->
                var filename = cacheManager.getCachedContentFileName(content)
                if(filename == null) // is not in cache
                {
                    Timber.e("Attachment content ${content.id} not in cache, downloading")
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

                val abs_path = cacheManager.getAbsolutePath(filename)
                appStateManager.save()

                enrichType(content)

                runOnUIThread {
                    output?.onOpenAttachment(abs_path, content.mimeType ?: "*/*")
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            runOnUIThread {
                output?.onOpenAttachmentError("Unknown error opening attachment ${input?.attachment}")
            }
        }
    }

    fun enrichType(content : Content)
    {
        var ext = content.fileExtension
        var mime = content.mimeType
        for(type in embeddedTypes)
        {
            // do we have a mime type? those are the bestest!!
            if(mime != null)
            {
                if(type.mimeType == mime) // recognized
                    return
            }
            else if(ext != null) // narp go with the oldschool windows file extension
            {
                if(type.fileExtension == ext) {
                    content.mimeType = type.mimeType // enrich with the mimetype if we only have file ext
                    return
                }
            }
        }
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