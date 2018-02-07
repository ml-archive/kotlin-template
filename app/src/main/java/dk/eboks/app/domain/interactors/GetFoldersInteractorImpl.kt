package dk.eboks.app.domain.interactors

import dk.eboks.app.R
import dk.eboks.app.domain.managers.ResourceManager
import dk.eboks.app.domain.models.Folder
import dk.eboks.app.domain.repositories.FoldersRepository
import dk.eboks.app.domain.repositories.RepositoryException
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by bison on 01/02/18.
 */
class GetFoldersInteractorImpl(executor: Executor, val foldersRepository: FoldersRepository, val resourceManager: ResourceManager) : BaseInteractor(executor), GetFoldersInteractor {
    override var output: GetFoldersInteractor.Output? = null
    override var input: GetFoldersInteractor.Input? = null

    var systemFolderTypes : Array<String> = resourceManager.getStringArray(R.array.systemFolderTypes)

    override fun execute() {
        try {
            val folders = foldersRepository.getFolders(input?.cached ?: true)
            val system = ArrayList<Folder>()
            val user = ArrayList<Folder>()
            folders.forEach { f ->
                if(isInStringArray(f.type, systemFolderTypes)) {
                    system.add(f)
                    if(f.type == "inbox")
                    {
                        for(ib_f in f.folders)
                        {
                            if(ib_f.type == "folder")
                                user.add(ib_f)
                        }
                    }
                }
                else
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