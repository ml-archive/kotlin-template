package dk.eboks.app.presentation.ui.channels.components.content.ekey

import com.google.gson.Gson
import dk.eboks.app.domain.interactors.ekey.GetEKeyMasterkeyInteractor
import dk.eboks.app.domain.interactors.ekey.GetEKeyVaultInteractor
import dk.eboks.app.domain.interactors.ekey.SetEKeyMasterkeyInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.channel.ekey.*
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import dk.nodes.locksmith.core.preferences.EncryptedPreferences
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class EkeyComponentPresenter @Inject constructor(val appState: AppStateManager, val getMasterkeyInteractor: GetEKeyMasterkeyInteractor,
                                                 val setMasterKeyInteractor: SetEKeyMasterkeyInteractor,
                                                 val getEKeyVaultInteractor: GetEKeyVaultInteractor,
                                                 val encryptedPreferences: EncryptedPreferences,
                                                 val gson: Gson)
    : EkeyComponentContract.Presenter, BasePresenterImpl<EkeyComponentContract.View>(),
        GetEKeyMasterkeyInteractor.Output, SetEKeyMasterkeyInteractor.Output, GetEKeyVaultInteractor.Output {
    init {
        getMasterkeyInteractor.output = this
        setMasterKeyInteractor.output = this
        getEKeyVaultInteractor.output = this
    }

    override fun onGetEKeyVaultSuccess() {
        Timber.d("success")
    }

    override fun onGetEKeyVaultError(viewError: ViewError) {
        Timber.d("failure")
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

    override fun storeMasterkey(masterKey: String) {
        appState.state?.currentUser?.let {
            encryptedPreferences.putString("ekey_${it.id}", masterKey)
        }
    }

    override fun setMasterkey(hash: String, encrypted: String) {
        setMasterKeyInteractor.input = SetEKeyMasterkeyInteractor.Input(encrypted, hash)
        setMasterKeyInteractor.run()
    }

    override fun getMasterkey() {
        getMasterkeyInteractor.run()
    }

    override fun getKeys(signatureTime: String, signature: String) {
        getEKeyVaultInteractor.input = GetEKeyVaultInteractor.Input(signatureTime, signature)
        getEKeyVaultInteractor.run()
    }

    override fun getKeys() {
        runAction { v ->
            v.showKeys(createMocks())
        }
    }

    private fun createMocks() : List<BaseEkey>{
        val keyList = mutableListOf<BaseEkey>()
        keyList.add(Login("test1@gmail.com", "gmailPW1", "Gmail", "Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet"))
        keyList.add(Pin("Peter","1234", "Visa", null))
        keyList.add(Login("test1@hotmail.com", "hotmailPW1", "Hotmail", null))
        keyList.add(Login("test1@hotmail.com", "hotmailPW1", "Hotmail", null))
        keyList.add(Login("test1@hotmail.com", "hotmailPW1", "Hotmail", null))
        keyList.add(Login("test1@hotmail.com", "hotmailPW1", "Hotmail", null))
        keyList.add(Note("Summerhouse", "Lorem ipsum dolor sit amet"))
        keyList.add(Pin("Knud","4321", "MasterCard", null))
        keyList.add(Ekey("1234","Ekey", null))

        val temp = gson.toJson(keyList)
        Timber.d(temp)

        return keyList
    }
}