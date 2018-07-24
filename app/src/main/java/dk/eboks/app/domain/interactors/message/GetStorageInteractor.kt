package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.StorageInfo
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface GetStorageInteractor : Interactor {
    var output : Output?

    interface Output {
        fun onGetStorage(storageInfo : StorageInfo )
        fun onGetStorageError(error : ViewError)
    }
}