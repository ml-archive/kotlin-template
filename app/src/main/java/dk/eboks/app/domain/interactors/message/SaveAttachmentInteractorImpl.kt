package dk.eboks.app.domain.interactors.message

import android.Manifest
import dk.eboks.app.domain.interactors.InteractorException
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.FileCacheManager
import dk.eboks.app.domain.managers.PermissionManager
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.nio.file.Files.exists



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

                if(permissionManager.requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
                {

                }
                else
                    throw(InteractorException("User refused permission"))


                val ext_filename = filename?.replace("[^a-zA-Z0-9\\.\\-]", "_")

                Timber.e("Generated safe filename $ext_filename")
                //copyFile()

                /*
                val perms = permissionManager.requestPermissions(listOf(Manifest.permission.READ_EXTERNAL_STORAGE))
                perms?.let {
                    perms.forEach {
                        Timber.e("$it")
                    }
                }
                */

                runOnUIThread {
                    output?.onSaveAttachment(filename!!)
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            runOnUIThread {
                output?.onSaveAttachmentError(e.message ?: "Unknown error saving attachment ${input?.attachment}")
            }
        }
    }

    @Throws(IOException::class)
    fun copyFile(sourceFile: File, destFile: File) {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs()

        if (!destFile.exists()) {
            destFile.createNewFile()
        }

        var source: FileChannel? = null
        var destination: FileChannel? = null

        try {
            source = FileInputStream(sourceFile).getChannel()
            destination = FileOutputStream(destFile).getChannel()
            destination!!.transferFrom(source, 0, source!!.size())
        } finally {
            if (source != null) {
                source!!.close()
            }
            if (destination != null) {
                destination!!.close()
            }
        }
    }

}