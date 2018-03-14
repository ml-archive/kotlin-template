package dk.eboks.app.domain.interactors.folder

import dk.eboks.app.domain.managers.ResourceManager
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.repositories.FoldersRepository
import dk.eboks.app.domain.exceptions.RepositoryException
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
 * Created by bison on 01/02/18.
 */
class GetFoldersInteractorImpl(executor: Executor, val foldersRepository: FoldersRepository, val resourceManager: ResourceManager) : BaseInteractor(executor), GetFoldersInteractor {
    override var output: GetFoldersInteractor.Output? = null
    override var input: GetFoldersInteractor.Input? = null

    override fun execute() {
        try {
            val folders = foldersRepository.getFolders(input?.cached ?: true)
            Timber.e("Got folders $folders")
            val system = ArrayList<Folder>()
            val user = ArrayList<Folder>()
            folders.forEach { f ->
                if(f.type.isSystemFolder()) {
                    system.add(f)
                }
                if(f.type == FolderType.FOLDER)
                    user.add(f)
            }
            runOnUIThread {
                output?.onGetSystemFolders(system)
                output?.onGetFolders(user)
            }
        } catch (e: RepositoryException) {
            runOnUIThread {
                output?.onGetFoldersError(e.message ?: "Unknown error")
            }
        }
    }

    fun isInStringArray(str : String, array : Array<String>) : Boolean
    {
        for(cur_str in array)
        {
            if(str.contentEquals(cur_str))
                return true
        }
        return false
    }
}