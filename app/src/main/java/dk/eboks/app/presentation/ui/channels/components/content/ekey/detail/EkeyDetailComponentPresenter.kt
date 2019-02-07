package dk.eboks.app.presentation.ui.channels.components.content.ekey.detail

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dk.eboks.app.domain.interactors.ekey.SetEKeyVaultInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.channel.ekey.BaseEkey
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.network.util.EKeyDeserializer
import dk.eboks.app.network.util.EKeySerializer
import dk.nodes.arch.presentation.base.BasePresenterImpl
import dk.nodes.locksmith.core.encryption.handlers.EncryptionHandlerImpl
import dk.nodes.locksmith.core.encryption.providers.AesCBCPasswordKeyProviderImpl
import dk.nodes.locksmith.core.preferences.EncryptedPreferences
import dk.nodes.locksmith.core.util.HashingUtils
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class EkeyDetailComponentPresenter @Inject constructor(
    private val appState: AppStateManager,
    private val encryptedPreferences: EncryptedPreferences,
    private val setEKeyVaultInteractor: SetEKeyVaultInteractor
) : EkeyDetailComponentContract.Presenter, BasePresenterImpl<EkeyDetailComponentContract.View>(),
    SetEKeyVaultInteractor.Output {

    private val gson: Gson

    init {
        setEKeyVaultInteractor.output = this
        val baseEkeyListType = object : TypeToken<MutableList<BaseEkey>>() {}.type
        gson = GsonBuilder()
            .registerTypeAdapter(baseEkeyListType, EKeyDeserializer())
            .registerTypeAdapter(BaseEkey::class.java, EKeySerializer())
            .create()
    }

    override fun putVault(items: MutableList<BaseEkey>, item: BaseEkey) {
        appState.state?.currentUser?.let {
            val masterKey = encryptedPreferences.getString("ekey_${it.id}", null)
            masterKey?.let { mk ->
                items.add(item)
                setVault(mk, items)
            }
        }
    }

    override fun onSetEKeyVaultSuccess() {
        Timber.d("onSetEKeyVaultSuccess")
        runAction { view -> view.onSuccess() }
    }

    override fun onAuthError(retryCount: Int) {
        appState.state?.currentUser?.let {
            encryptedPreferences.remove("ekey_${it.id}")
        }

        runAction { view -> view.showPinView() }
    }

    override fun onSetEKeyVaultError(viewError: ViewError) {
        runAction { view -> view.showErrorDialog(viewError) }
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
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")
        val dateFormat2 = SimpleDateFormat("yyyyMMddHHmmss", Locale.GERMAN)
        dateFormat2.timeZone = TimeZone.getTimeZone("GMT")
        val date = Date()
        val time = dateFormat.format(date)
        val time2 = dateFormat2.format(date)
        val hash = HashingUtils.hmacSha256(keyHash, time2)

        return Pair(time, hash)
    }
}