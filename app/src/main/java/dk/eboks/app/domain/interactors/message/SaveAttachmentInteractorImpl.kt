package dk.eboks.app.domain.interactors.message

import android.Manifest
import dk.eboks.app.domain.exceptions.InteractorException
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.FileCacheManager
import dk.eboks.app.domain.managers.PermissionManager
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor


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
                filename.guard { throw InteractorException("Cached content $filename could not be find") }

                if(!permissionManager.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                {
                    throw(InteractorException("User refused permission"))
                }
                if(!cacheManager.isExternalStorageWritable())
                    throw(InteractorException("External storage is currently unavailable"))
                val public_filename = cacheManager.copyContentToExternalStorage(content)
                public_filename?.let {
                    runOnUIThread {
                        output?.onSaveAttachment(it)
                    }
                }.guard{ throw(InteractorException("Copying cached file to downloads dir failed"))}
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            runOnUIThread {
                output?.onSaveAttachmentError(ViewError(title = Translation.error.genericStorageTitle, message = Translation.error.genericStorageMessage))
            }
        }
    }

}