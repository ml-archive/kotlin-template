package dk.eboks.app.domain.interactors.ekey

import dk.eboks.app.domain.models.channel.ekey.EKeyGetMasterkeyResponse
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface GetEKeyMasterkeyInteractor: Interactor {
    interface Output {
        fun onGetEKeyMasterkeySuccess(masterkey: EKeyGetMasterkeyResponse)
        fun onGetEKeyMasterkeyError(viewError: ViewError)
        fun onGetEkeyMasterkeyNotFound()
    }

    var output: Output?
}