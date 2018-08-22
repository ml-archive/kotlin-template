package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
 * Created by Christian on 5/15/2018.
 * @author   Christian
 * @since    5/15/2018.
 */
class SaveReceiptInteractorImpl(executor: Executor, private val api: Api) : BaseInteractor(executor), SaveReceiptInteractor {
    override var output: SaveReceiptInteractor.Output? = null
    override var input: SaveReceiptInteractor.Input? = null

    override fun execute() {
        try {
            input?.let { args->
                val result = api.saveStoreboxReceipt(args.receiptId, args.folderId).execute()
                if(result.isSuccessful) {
                    runOnUIThread {
                        output?.onSaveReceiptSuccess()
                    }
                }
                else
                {
                    runOnUIThread {
                        output?.onSaveReceiptError(ViewError())
                    }
                }
            }
        } catch (t: Throwable) {
            runOnUIThread {
                Timber.e(t)
                output?.onSaveReceiptError(exceptionToViewError(t))
            }
        }
    }

}