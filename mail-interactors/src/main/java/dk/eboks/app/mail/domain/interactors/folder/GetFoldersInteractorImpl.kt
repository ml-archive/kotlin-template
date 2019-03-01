package dk.eboks.app.mail.domain.interactors.folder

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderMode
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.folder.isSystemFolder
import dk.eboks.app.domain.repositories.FoldersRepository
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 01/02/18.
 */
internal class GetFoldersInteractorImpl @Inject constructor(
    executor: Executor,
    private val foldersRepository: FoldersRepository
) : BaseInteractor(executor), GetFoldersInteractor {
    override var output: GetFoldersInteractor.Output? = null
    override var input: GetFoldersInteractor.Input? = null

    override fun execute() {
        val modeType = input?.pickermode ?: FolderMode.NORMAL
        if (modeType == FolderMode.SELECT) {
            val folders = foldersRepository.getFolders(input?.cached ?: true, input?.userId)
            runOnUIThread {
                val pickerfolders = ArrayList<Folder>()
                folders.forEach { f ->

                    if (f.type == FolderType.FOLDER) {
                        pickerfolders.add(f)
                    }
                    if (f.type == FolderType.INBOX) {
                        pickerfolders.add(0, f)
                    }
                }
                output?.onGetFolders(pickerfolders)
            }
        } else {
            try {
                val folders = foldersRepository.getFolders(input?.cached ?: true, input?.userId)
                Timber.e("Got folders $folders")
                val system = ArrayList<Folder>()
                val user = ArrayList<Folder>()
                folders.forEach { f ->
                    if (f.type?.isSystemFolder() == true) {
                        system.add(f)
                    }
                    if (f.type == FolderType.FOLDER)
                        user.add(f)
                }
                runOnUIThread {
                    output?.onGetSystemFolders(system)
                    output?.onGetFolders(user)
                }
            } catch (t: Throwable) {
                runOnUIThread {
                    output?.onGetFoldersError(exceptionToViewError(t))
                }
            }
        }
    }

    fun isInStringArray(str: String, array: Array<String>): Boolean {
        for (cur_str in array) {
            if (str.contentEquals(cur_str))
                return true
        }
        return false
    }
}