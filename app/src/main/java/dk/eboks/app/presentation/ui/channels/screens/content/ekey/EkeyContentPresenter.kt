package dk.eboks.app.presentation.ui.channels.screens.content.ekey

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dk.eboks.app.domain.interactors.ekey.GetEKeyMasterkeyInteractor
import dk.eboks.app.domain.interactors.ekey.GetEKeyVaultInteractor
import dk.eboks.app.domain.interactors.ekey.SetEKeyMasterkeyInteractor
import dk.eboks.app.domain.interactors.ekey.SetEKeyVaultInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.ekey.BaseEkey
import dk.eboks.app.domain.models.channel.ekey.Ekey
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class EkeyContentPresenter @Inject constructor(
    private val appState: AppStateManager,
    private val encryptedPreferences: EncryptedPreferences,
    private val getMasterkeyInteractor: GetEKeyMasterkeyInteractor,
    private val setMasterKeyInteractor: SetEKeyMasterkeyInteractor,
    private val getEKeyVaultInteractor: GetEKeyVaultInteractor,
    private val setEKeyVaultInteractor: SetEKeyVaultInteractor
) : EkeyContentContract.Presenter, BasePresenterImpl<EkeyContentContract.View>(),
    GetEKeyMasterkeyInteractor.Output, SetEKeyMasterkeyInteractor.Output,
    GetEKeyVaultInteractor.Output, SetEKeyVaultInteractor.Output {

    private var gson: Gson
    private var masterKey: String? = null
    override var pin: String? = null
    private var keys: ArrayList<BaseEkey> = arrayListOf()
    private var hasRetried: Boolean = false

    init {
        getMasterkeyInteractor.output = this
        setMasterKeyInteractor.output = this
        getEKeyVaultInteractor.output = this
        setEKeyVaultInteractor.output = this
        val baseEkeyListType = object : TypeToken<MutableList<BaseEkey>>() {}.type
        gson = GsonBuilder()
            .registerTypeAdapter(baseEkeyListType, EKeyDeserializer())
            .registerTypeAdapter(BaseEkey::class.java, EKeySerializer())
            .create()
    }

    override fun onSetEKeyVaultError(viewError: ViewError) {
        if (hasRetried) {
            runAction { view -> view.showErrorDialog(viewError) }
            hasRetried = false
        } else {
            hasRetried = true
            appState.state?.currentUser?.let {
                encryptedPreferences.remove("ekey_${it.id}")
            }
            getData()
        }
    }

    override fun onSetEKeyVaultSuccess() {
        runAction { v -> v.showKeys(keys) }
    }

    override fun onGetEKeyVaultSuccess(vault: String) {
        masterKey?.let { mk ->
            val decrypted = decryptVault(mk, vault)
            decrypted?.let { decryptedString ->
                val baseEkeyListType = object : TypeToken<MutableList<BaseEkey>>() {}.type
                keys = gson.fromJson<ArrayList<BaseEkey>>(decryptedString, baseEkeyListType)
                runAction { v -> v.showKeys(keys) }
            }
        }
    }

    override fun onGetEKeyVaultNotFound() {
        val keyList = arrayListOf<BaseEkey>()
        pin?.let {
            keyList.add(Ekey(it, "Ekey", null))
        }

        // Encrypt
        masterKey?.let {
            keys = keyList
            setVault(it, keyList)
        }
    }

    override fun onGetEKeyVaultError(viewError: ViewError) {
        Timber.d("onGetEKeyVaultError")
        runAction { view -> view.showErrorDialog(viewError) }
    }

    override fun onAuthError(retryCount: Int) {
        appState.state?.currentUser?.let {
            encryptedPreferences.remove("ekey_${it.id}")
        }

        runAction { view -> view.showPinView(false) }
    }

    override fun onSetEKeyMasterkeySuccess(masterKey: String) {
        this.masterKey = masterKey
        handleMasterKeySuccess(masterKey)
    }

    override fun onSetEKeyMasterkeyError(viewError: ViewError) {
        Timber.d("onSetEKeyMasterkeyError")

        runAction { view -> view.showErrorDialog(viewError) }
    }

    override fun onGetEKeyMasterkeySuccess(masterKeyResponse: String?) {
        if (pin != null) {
            masterKeyResponse?.let {
                val handler = EncryptionHandlerImpl(AesCBCPasswordKeyProviderImpl(pin))
                handler.init()

                try {
                    masterKey = String(handler.decrypt(it), charset("UTF-8"))
                    Timber.d("Decrypted from backend: $masterKey")

                    handleMasterKeySuccess(masterKey!!)
                } catch (e: Exception) {
                    runAction { view ->
                        view.showErrorDialog(
                            ViewError(
                                title = Translation.error.eKeyDecryptionFailedTitle,
                                message = Translation.error.eKeyDecryptionFailedMessage
                            )
                        )
                        view.showPinView(false)
                    }
                }
            }.guard {
                //            generateNewKeyAndSend(pin)
                runAction { view -> view.showPinView(true) }
            }
        } else {
            runAction { view -> view.showPinView(masterKeyResponse.isNullOrEmpty()) }
        }
    }

    override fun onGetEkeyMasterkeyNotFound() {
        if (pin != null) {
            generateNewKeyAndSend(pin)
        } else {
            runAction { view -> view.showPinView(true) }
        }
    }

    override fun onGetEKeyMasterkeyError(viewError: ViewError) {
        runAction { view -> view.onGetMasterkeyError(viewError) }
    }

    // HELPER METHODS
    override fun putVault(items: ArrayList<BaseEkey>) {
        appState.state?.currentUser?.let {
            val masterKey = encryptedPreferences.getString("ekey_${it.id}", null)
            masterKey?.let { mk ->
                keys = items
                setVault(mk, items)
            }
        }
    }

    private fun setVault(masterKey: String, keyList: MutableList<BaseEkey>) {
        val handler = EncryptionHandlerImpl(AesCBCPasswordKeyProviderImpl(masterKey))
        handler.init()

        val vault = gson.toJson(keyList)
        val encrypted = handler.encrypt(vault.toByteArray(charset("UTF-8")))
        // post vault
        val signature = getSignatureAndSignatureTime(masterKey)

        setEKeyVaultInteractor.input =
            SetEKeyVaultInteractor.Input(encrypted, signature.first, signature.second, 0)
        setEKeyVaultInteractor.run()
    }

    private fun getSignatureAndSignatureTime(masterKey: String): Pair<String, String> {
        val keyHash = HashingUtils.sha256AsBase64(masterKey)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'", Locale.GERMAN)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val dateFormat2 = SimpleDateFormat("yyyyMMddHHmmss", Locale.GERMAN)
        dateFormat2.timeZone = TimeZone.getTimeZone("UTC")
        val date = Date()
        val time = dateFormat.format(date)
        val time2 = dateFormat2.format(date)
        val hash = HashingUtils.hmacSha256(keyHash, time2)

        return Pair(time, hash)
    }

    private fun decryptVault(masterKey: String, vault: String): String? {
        val handler = EncryptionHandlerImpl(AesCBCPasswordKeyProviderImpl(masterKey))
        handler.init()

        return try {
            String(handler.decrypt(vault), charset("UTF-8"))
        } catch (e: Exception) {
            runAction { view ->
                view.showErrorDialog(
                    ViewError(
                        title = Translation.error.eKeyDecryptionFailedTitle,
                        message = Translation.error.eKeyDecryptionFailedMessage
                    )
                )
                view.showPinView(false)
            }
            null
        }
    }

    private fun handleMasterKeySuccess(masterKey: String) {
        storeMasterkey(masterKey)
        getVault(masterKey)
    }

    private fun storeMasterkey(masterKey: String) {
        appState.state?.currentUser?.let {
            encryptedPreferences.putString("ekey_${it.id}", masterKey)
        }
    }

    private fun getVault(masterKey: String) {
        val time = getSignatureAndSignatureTime(masterKey)
        getEKeyVaultInteractor.input = GetEKeyVaultInteractor.Input(time.first, time.second, 0)
        getEKeyVaultInteractor.run()
    }

    private fun generateNewKeyAndSend(pin: String?) {
        Timber.d("pin: $pin")
        val key = RandomUtils.generateRandomString(32)
        Timber.d("gen key: $key")

        val hashed = HashingUtils.sha256AsBase64(key)
        Timber.d("Hash: $hashed")

        val handler = EncryptionHandlerImpl(AesCBCPasswordKeyProviderImpl(pin))
        handler.init()

        val encrypted = handler.encrypt(key.toByteArray(charset("UTF-8")))
        Timber.d("Encrypted: $encrypted")

        // send key to backend
        setMasterkey(hashed, encrypted, key)
    }

    private fun setMasterkey(hash: String, encrypted: String, unencrypted: String) {
        setMasterKeyInteractor.input =
            SetEKeyMasterkeyInteractor.Input(encrypted, hash, unencrypted)
        setMasterKeyInteractor.run()
    }

    private fun getMasterkeyFromBackend() {
        getMasterkeyInteractor.input = GetEKeyMasterkeyInteractor.Input()
        getMasterkeyInteractor.run()
    }

    override fun getData() {
        appState.state?.currentUser?.let {
            val mk = encryptedPreferences.getString("ekey_${it.id}", "")
            if (!mk.isEmpty()) {
                masterKey = mk
                getVault(mk)
            } else {
                getMasterkeyFromBackend()
            }
        }
    }
}