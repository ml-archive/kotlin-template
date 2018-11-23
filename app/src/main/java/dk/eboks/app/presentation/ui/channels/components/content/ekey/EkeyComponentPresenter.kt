package dk.eboks.app.presentation.ui.channels.components.content.ekey

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dk.eboks.app.domain.interactors.ekey.*
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.channel.ekey.*
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.network.util.EKeyDeserializer
import dk.eboks.app.network.util.EKeySerializer
import dk.eboks.app.util.guard
import dk.nodes.arch.presentation.base.BasePresenterImpl
import dk.nodes.locksmith.core.encryption.handlers.EncryptionHandlerImpl
import dk.nodes.locksmith.core.encryption.providers.AesCBCPasswordKeyProviderImpl
import dk.nodes.locksmith.core.preferences.EncryptedPreferences
import dk.nodes.locksmith.core.util.HashingUtils
import dk.nodes.locksmith.core.util.RandomUtils
import timber.log.Timber
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class EkeyComponentPresenter @Inject constructor(val appState: AppStateManager, val getMasterkeyInteractor: GetEKeyMasterkeyInteractor,
                                                 val setMasterKeyInteractor: SetEKeyMasterkeyInteractor,
                                                 val deleteMasterKeyInteractor: DeleteEKeyMasterkeyInteractor,
                                                 val getEKeyVaultInteractor: GetEKeyVaultInteractor,
                                                 val encryptedPreferences: EncryptedPreferences,
                                                 val setEKeyVaultInteractor: SetEKeyVaultInteractor,
                                                 val deleteEKeyVaultInteractor: DeleteEKeyVaultInteractor)
    : EkeyComponentContract.Presenter, BasePresenterImpl<EkeyComponentContract.View>(),
        GetEKeyMasterkeyInteractor.Output, SetEKeyMasterkeyInteractor.Output, GetEKeyVaultInteractor.Output {

    val gson: Gson
    var masterKey: String? = null

    init {
        getMasterkeyInteractor.output = this
        setMasterKeyInteractor.output = this
        getEKeyVaultInteractor.output = this
        val baseEkeyListType = object : TypeToken<MutableList<BaseEkey>>() {}.type
        gson = GsonBuilder()
                .registerTypeAdapter(baseEkeyListType, EKeyDeserializer())
                .registerTypeAdapter(BaseEkey::class.java, EKeySerializer())
                .create()
    }

    private var keys: MutableList<BaseEkey> = mutableListOf()

    override fun onGetEKeyVaultSuccess(vault: String) {
        masterKey?.let {
            val decrypted = decryptVault(it, vault)
            val baseEkeyListType = object : TypeToken<MutableList<BaseEkey>>() {}.type
            keys = gson.fromJson<MutableList<BaseEkey>>(decrypted, baseEkeyListType)
            runAction { v -> v.showKeys(keys) }
        }
    }

    override fun onGetEKeyVaultNotFound(pin: String) {
        val keyList = mutableListOf<BaseEkey>()
        keyList.add(Ekey(pin, "Ekey", null))

        //Encrypt
        masterKey?.let {
            setVault(it, keyList)
            runAction { view -> view.showKeys(keyList) }
        }
    }

    override fun onGetEKeyVaultError(viewError: ViewError) {
        Timber.d("onGetEKeyVaultError")
    }

    override fun onSetEKeyMasterkeySuccess(masterKey: String, pin: String) {
        onGetEKeyMasterkeySuccess(masterKey, pin)
    }

    override fun onSetEKeyMasterkeyError(viewError: ViewError) {
        Timber.d("onSetEKeyMasterkeyError")
    }

    override fun onGetEKeyMasterkeySuccess(masterKeyResponse: String?, pin: String) {
        masterKeyResponse?.let {
            val handler = EncryptionHandlerImpl(AesCBCPasswordKeyProviderImpl(pin))
            handler.init()

            try {
                masterKey = String(handler.decrypt(it), charset("UTF-8"))
                Timber.d("Decrypted from backend: $masterKey")

                storeMasterkey(masterKey!!)
                getVault(masterKey!!, pin)
            } catch (e: Exception) {
                runAction { view -> view.showErrorDialog(ViewError()) }
            }

        }.guard {
            generateNewKeyAndSend(pin)
        }
    }

    override fun onGetEkeyMasterkeyNotFound(pin: String) {
        generateNewKeyAndSend(pin)
    }

    override fun onGetEKeyMasterkeyError(viewError: ViewError) {
        runAction { view -> view.onGetMasterkeyError(viewError) }
    }

    override fun storeMasterkey(masterKey: String) {
        appState.state?.currentUser?.let {
            encryptedPreferences.putString("ekey_${it.id}", masterKey)
        }
    }

    override fun setMasterkey(hash: String, encrypted: String, pin: String) {
        setMasterKeyInteractor.input = SetEKeyMasterkeyInteractor.Input(encrypted, hash, pin)
        setMasterKeyInteractor.run()
    }

    override fun getMasterkey(pin: String) {
        getMasterkeyInteractor.input = GetEKeyMasterkeyInteractor.Input(pin)
        getMasterkeyInteractor.run()
    }

    override fun getVault(masterKey: String, pin: String) {
        val time = getSignatureAndSignatureTime(masterKey)
        getEKeyVaultInteractor.input = GetEKeyVaultInteractor.Input(time.first, time.second, pin)
        getEKeyVaultInteractor.run()
    }

    override fun setVault(masterKey: String, keyList: MutableList<BaseEkey>) {
        val handler = EncryptionHandlerImpl(AesCBCPasswordKeyProviderImpl(masterKey))
        handler.init()

        val vault = gson.toJson(keyList)
        val encrypted = handler.encrypt(vault.toByteArray(charset("UTF-8")))
        //post vault
        val signature = getSignatureAndSignatureTime(masterKey)

        setEKeyVaultInteractor.input = SetEKeyVaultInteractor.Input(encrypted, signature.first, signature.second)
        setEKeyVaultInteractor.run()
    }

    override fun deleteMasterKey() {
        deleteMasterKeyInteractor.run()
    }

    override fun deleteVault(signature: String, signatureTime: String) {
        deleteEKeyVaultInteractor.input = DeleteEKeyVaultInteractor.Input(signature, signatureTime)
        deleteEKeyVaultInteractor.run()
    }

    override fun decryptVault(masterKey: String, vault: String): String {
        val handler = EncryptionHandlerImpl(AesCBCPasswordKeyProviderImpl(masterKey))
        handler.init()

        val decrypted = String(handler.decrypt(vault), charset("UTF-8"))
        return decrypted
    }

    private fun getSignatureAndSignatureTime(masterKey: String): Pair<String, String> {
        val keyHash = HashingUtils.sha256AsBase64(masterKey)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'", Locale.GERMAN)
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")
        val dateFormat2 = SimpleDateFormat("yyyyMMddHHmmss", Locale.GERMAN)
        dateFormat2.timeZone = TimeZone.getTimeZone("GMT")
        val date = Date()
        val time = dateFormat.format(date)
        val time2 = dateFormat2.format(date)
        val hash = HashingUtils.hmacSha256(keyHash, time2)

        return Pair(time, hash)
    }

    private fun generateNewKeyAndSend(pin: String) {
        Timber.d("pin: $pin")
        val key = RandomUtils.generateRandomString(32)
        Timber.d("gen key: $key")

        val hashed = HashingUtils.sha256AsBase64(key)
        Timber.d("Hash: $hashed")

        val handler = EncryptionHandlerImpl(AesCBCPasswordKeyProviderImpl(pin))
        handler.init()

        val encrypted = handler.encrypt(key.toByteArray(charset("UTF-8")))
        Timber.d("Encrypted: $encrypted")

        //send key to backend
        setMasterkey(hashed, encrypted, pin)
    }

    override fun getKeys() {
        runAction { v ->
            v.showKeys(createMocks())
        }
    }

    override fun getKeyList(): MutableList<BaseEkey> {
        return keys
    }

    private fun createMocks(): List<BaseEkey> {
        val keyList = mutableListOf<BaseEkey>()
        keyList.add(Login("test1@gmail.com", "gmailPW1", "Gmail", "Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet"))
        keyList.add(Pin("Peter", "1234", "Visa", null))
        keyList.add(Login("test1@hotmail.com", "hotmailPW1", "Hotmail", null))
        keyList.add(Login("test1@hotmail.com", "hotmailPW1", "Hotmail", null))
        keyList.add(Login("test1@hotmail.com", "hotmailPW1", "Hotmail", null))
        keyList.add(Login("test1@hotmail.com", "hotmailPW1", "Hotmail", null))
        keyList.add(Note("Summerhouse", "Lorem ipsum dolor sit amet"))
        keyList.add(Pin("Knud", "4321", "MasterCard", null))
        keyList.add(Ekey("1234", "Ekey", null))

        val temp = gson.toJson(keyList)
        Timber.d(temp)

        return keyList
    }
}