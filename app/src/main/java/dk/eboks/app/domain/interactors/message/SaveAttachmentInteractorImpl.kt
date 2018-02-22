package dk.eboks.app.domain.interactors.message

import android.Manifest
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.FileCacheManager
import dk.eboks.app.domain.managers.PermissionManager
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
 * Created by bison on 01/02/18.
 */
class SaveAttachmentInteractorImpl(executor: Executor, val appStateManager: AppStateManager,
                                   val cacheManager: FileCacheManager, val permissionManager: PermissionManager)
    : BaseInteractor(executor), SaveAttachmentInteractor {

    override var output: SaveAttachmentInteractor.Output? = null
    override var input: SaveAttachmentInteractor.Input? = null

    override fun execute() {
        try {
            input?.attachment?.let { content->
                var filename = cacheManager.getCachedContentFileName(content)

                val perms = permissionManager.requestPermissions(listOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                perms?.let {
                    perms.forEach {
                        Timber.e("$it")
                    }
                }

                runOnUIThread {
                    output?.onSaveAttachment(filename!!)
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            runOnUIThread {
                output?.onSaveAttachmentError("Unknown error saving attachment ${input?.attachment}")
            }
        }
    }

}