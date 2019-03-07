package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.domain.managers.DownloadManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber
import javax.inject.Inject

internal class ShareReceiptInteractorImpl @Inject constructor(
    executor: Executor,
    private val downloadManager: DownloadManager
) : BaseInteractor(executor), ShareReceiptInteractor {
    override var output: ShareReceiptInteractor.Output? = null
    override var input: ShareReceiptInteractor.Input? = null

    override fun execute() {
        try {
            input?.let { args ->
                val result = downloadManager.downloadReceiptContent(args.receiptId)
                result?.let {
                    runOnUIThread {
                        output?.onShareReceiptSuccess(result)
                    }
                }.guard {
                    runOnUIThread {
                        output?.onShareReceiptError(ViewError())
                    }
                }
            }
        } catch (t: Throwable) {
            runOnUIThread {
                Timber.e(t)
                output?.onShareReceiptError(exceptionToViewError(t))
            }
        }
    }
}