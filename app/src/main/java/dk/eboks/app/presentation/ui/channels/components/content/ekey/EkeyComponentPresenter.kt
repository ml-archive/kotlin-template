package dk.eboks.app.presentation.ui.channels.components.content.ekey

import dk.eboks.app.domain.interactors.ekey.GetEKeyMasterkeyInteractor
import dk.eboks.app.domain.interactors.ekey.SetEKeyMasterkeyInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.channel.ekey.*
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class EkeyComponentPresenter @Inject constructor(val appState: AppStateManager, val getMasterkeyInteractor: GetEKeyMasterkeyInteractor,
                                                 val setMasterKeyInteractor: SetEKeyMasterkeyInteractor)
    : EkeyComponentContract.Presenter, BasePresenterImpl<EkeyComponentContract.View>(),
        GetEKeyMasterkeyInteractor.Output,
        SetEKeyMasterkeyInteractor.Output {
    init {
        getMasterkeyInteractor.output = this
        setMasterKeyInteractor.output = this
    }

    override fun onSetEKeyMasterkeySuccess() {
        Timber.d("success")
    }

    override fun onSetEKeyMasterkeyError(viewError: ViewError) {
        Timber.d("failure")
    }

    override fun onGetEKeyMasterkeySuccess(masterkey: EKeyGetMasterkeyResponse) {
        runAction { view -> view.onMasterkey(masterkey.masterkey) }
    }

    override fun onGetEKeyMasterkeyError(viewError: ViewError) {
        runAction { view -> view.onGetMasterkeyError(viewError) }
    }

    override fun setMasterkey(hash: String, encrypted: String) {
        setMasterKeyInteractor.input = SetEKeyMasterkeyInteractor.Input(encrypted, hash)
        setMasterKeyInteractor.run()
    }

    override fun getMasterkey() {
        getMasterkeyInteractor.run()
    }

    override fun getKeys() {
        runAction { v ->
            v.showKeys(createMocks())
        }
    }

    private fun createMocks() : List<Ekey>{
        val keyList = mutableListOf<Ekey>()
        keyList.add(Login("test1@gmail.com", "gmailPW1", "Gmail", "Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet"))
        keyList.add(Pin("Peter","1234", "Visa", null))
        keyList.add(Login("test1@hotmail.com", "hotmailPW1", "Hotmail", null))
        keyList.add(Login("test1@hotmail.com", "hotmailPW1", "Hotmail", null))
        keyList.add(Login("test1@hotmail.com", "hotmailPW1", "Hotmail", null))
        keyList.add(Login("test1@hotmail.com", "hotmailPW1", "Hotmail", null))
        keyList.add(Note("Summerhouse", "Lorem ipsum dolor sit amet"))
        keyList.add(Pin("Knud","4321", "MasterCard", null))

        return keyList
    }
}