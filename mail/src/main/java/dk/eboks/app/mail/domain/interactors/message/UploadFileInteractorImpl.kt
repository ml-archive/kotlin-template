package dk.eboks.app.mail.domain.interactors.message

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber
import javax.inject.Inject

internal class UploadFileInteractorImpl @Inject constructor(
    executor: Executor,
    private val messagesRepository: MessagesRepository
) :
    BaseInteractor(executor), UploadFileInteractor {
    override var output: UploadFileInteractor.Output? = null
    override var input: UploadFileInteractor.Input? = null

    override fun execute() {
        input?.let { args ->
            try {
                messagesRepository.uploadFileAsMessage(
                    args.folderId,
                    args.filename,
                    args.uriString,
                    args.mimetype
                ) { count ->
                    // Timber.e("Upload progress $count")
                    runOnUIThread {
                        output?.onUploadFileProgress(count)
                    }
                }
                runOnUIThread {
                    output?.onUploadFileComplete()
                }
            } catch (t: Throwable) {
                Timber.e(t)
                runOnUIThread {
                    output?.onUploadFileError(exceptionToViewError(t, shouldClose = true))
                }
            }
        }.guard {
            output?.onUploadFileError(ViewError())
        }
    }
}