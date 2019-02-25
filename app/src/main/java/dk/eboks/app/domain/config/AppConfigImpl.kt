package dk.eboks.app.domain.config

import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.shared.ResourceLink
import dk.eboks.app.presentation.ui.login.components.LoginComponentFragment
import dk.eboks.app.presentation.ui.login.components.providers.bankidno.BankIdNOComponentFragment
import dk.eboks.app.presentation.ui.login.components.providers.bankidse.BankIdSEComponentFragment
import dk.eboks.app.presentation.ui.login.components.providers.idporten.IdPortenComponentFragment
import dk.eboks.app.presentation.ui.login.components.providers.nemid.NemIdComponentFragment
import timber.log.Timber

// TODO this stuff should be downloaded from a url (on request of the customer) so that the app only contains
// one harded coded url ya'll
/**
 * Created by bison on 07/12/17.
 *
 * This class should not be written to at runtime, it presents a basic configuration for each of the
 * app flavors (DK, SE, NO)
 *
 */
object AppConfigImpl : AppConfig {

    private const val VERSION = BuildConfig.VERSION_NAME

    private val danish: Mode = Mode(
        urlPrefix = "DA-DK",
        countryCode = "DK",
        cprLength = 10,
        demoVideo = "https://youtu.be/8OmF6uHxfWU",

        environments = mapOf(
            "test" to Environments(
                apiUrl = "http://test401-mobile-api-dk.internal.e-boks.com/2/",
                authUrl = "http://test401-oauth-dk.internal.e-boks.com/1/connect/token",
                kspUrl = "https://demo-m.e-boks.dk/app/logon.aspx?logontype=",
                shortAuthId = "MobileApp-Short-id",
                shortAuthSecret = "MobileApp-Short-secret",
                longAuthId = "MobileApp-Long-id",
                longAuthSecret = "MobileApp-Long-secret",
                shortAuthCustomId = "MobileApp-Short-Custom-id",
                shortAuthCustomSecret = "MobileApp-Short-Custom-secret",
                longAuthCustomId = "MobileApp-Long-Custom-id",
                longAuthCustomSecret = "MobileApp-Long-Custom-secret"
            ),
            "demo" to Environments(
                apiUrl = "https://demo-mobile-api-dk.internal.e-boks.com/2/",
                authUrl = "https://demo-oauth-dk.internal.e-boks.com/1/connect/token",
                kspUrl = "https://demo-m.e-boks.dk/app/logon.aspx?logontype=",
                shortAuthId = "MobileApp-Short-id",
                shortAuthSecret = "MobileApp-Short-secret",
                longAuthId = "MobileApp-Long-id",
                longAuthSecret = "MobileApp-Long-secret",
                shortAuthCustomId = "MobileApp-Short-Custom-id",
                shortAuthCustomSecret = "MobileApp-Short-Custom-secret",
                longAuthCustomId = "MobileApp-Long-Custom-id",
                longAuthCustomSecret = "MobileApp-Long-Custom-secret"
            ),
            "production" to Environments(
                apiUrl = "https://mobile-api-dk.e-boks.com/2/",
                authUrl = "https://oauth-dk.e-boks.com/1/connect/token",
                kspUrl = "https://m.e-boks.dk/app/logon.aspx?logontype=",
                shortAuthId = "MobileApp-Short-id",
                shortAuthSecret = "Buz3YmYmjhDRM9R3",
                longAuthId = "MobileApp-Long-id",
                longAuthSecret = "TgtjcNpY9R9ffw8D",
                shortAuthCustomId = "MobileApp-Short-Custom-id",
                shortAuthCustomSecret = "QmaENW6MeYwwjzF5",
                longAuthCustomId = "MobileApp-Long-Custom-id",
                longAuthCustomSecret = "4ZLmEL2SY69MqGKs"
            )
        ),

        customTranslationUrl = "https://m.e-boks.dk/app/resources/android/eboks.android.$VERSION.json",
        alternativeLoginProviders = listOf("nemid")
    )

    private val norwegian: Mode = Mode(
        urlPrefix = "NB-NO",
        countryCode = "NO",
        cprLength = 11,
        demoVideo = "https://youtu.be/8OmF6uHxfWU",

        environments = mapOf(
            "test" to Environments(
                apiUrl = "http://test401-mobile-api-no.internal.e-boks.com/2/",
                authUrl = "http://test401-oauth-dk.internal.e-boks.com/1/connect/token",
                kspUrl = "https://demo-m.e-boks.no/app/logon.aspx?logontype=",
                shortAuthId = "MobileApp-Short-id",
                shortAuthSecret = "MobileApp-Short-secret",
                longAuthId = "MobileApp-Long-id",
                longAuthSecret = "MobileApp-Long-secret",
                shortAuthCustomId = "MobileApp-Short-Custom-id",
                shortAuthCustomSecret = "MobileApp-Short-Custom-secret",
                longAuthCustomId = "MobileApp-Long-Custom-id",
                longAuthCustomSecret = "MobileApp-Long-Custom-secret"
            ),
            "demo" to Environments(
                apiUrl = "https://demo-mobile-api-no.internal.e-boks.com/2/",
                authUrl = "https://demo-oauth-no.internal.e-boks.com/1/connect/token",
                kspUrl = "https://demo-m.e-boks.no/app/logon.aspx?logontype=",
                shortAuthId = "MobileApp-Short-id",
                shortAuthSecret = "MobileApp-Short-secret",
                longAuthId = "MobileApp-Long-id",
                longAuthSecret = "MobileApp-Long-secret",
                shortAuthCustomId = "MobileApp-Short-Custom-id",
                shortAuthCustomSecret = "MobileApp-Short-Custom-secret",
                longAuthCustomId = "MobileApp-Long-Custom-id",
                longAuthCustomSecret = "MobileApp-Long-Custom-secret"
            ),
            "demo2" to Environments(
                apiUrl = "https://demo2-mobile-api-no.internal.e-boks.com/2/",
                authUrl = "https://demo2-oauth-no.internal.e-boks.com/1/connect/token",
                kspUrl = "https://demo2-m.e-boks.no/app/logon.aspx?logontype=",
                shortAuthId = "MobileApp-Short-id",
                shortAuthSecret = "MobileApp-Short-secret",
                longAuthId = "MobileApp-Long-id",
                longAuthSecret = "MobileApp-Long-secret",
                shortAuthCustomId = "MobileApp-Short-Custom-id",
                shortAuthCustomSecret = "MobileApp-Short-Custom-secret",
                longAuthCustomId = "MobileApp-Long-Custom-id",
                longAuthCustomSecret = "MobileApp-Long-Custom-secret"
            ),
            "production" to Environments(
                apiUrl = "https://mobile-api-no.e-boks.com/2/",
                authUrl = "https://oauth-no.e-boks.com/1/connect/token",
                kspUrl = "https://m.e-boks.no/app/logon.aspx?logontype=",
                shortAuthId = "MobileApp-Short-id",
                shortAuthSecret = "Buz3YmYmjhDRM9R3",
                longAuthId = "MobileApp-Long-id",
                longAuthSecret = "TgtjcNpY9R9ffw8D",
                shortAuthCustomId = "MobileApp-Short-Custom-id",
                shortAuthCustomSecret = "QmaENW6MeYwwjzF5",
                longAuthCustomId = "MobileApp-Long-Custom-id",
                longAuthCustomSecret = "4ZLmEL2SY69MqGKs"
            )
        ),

        customTranslationUrl = "https://m.e-boks.no/app/resources/android/eboks.android.$VERSION.json",
        alternativeLoginProviders = listOf("idporten", "bankid_no")
    )

    private val swedish: Mode = Mode(
        urlPrefix = "SV-SE",
        countryCode = "SE",
        cprLength = 12,
        demoVideo = "https://youtu.be/8OmF6uHxfWU",

        environments = mapOf(
            "test" to Environments(
                apiUrl = "http://test401-mobile-api-se.internal.e-boks.com/2/",
                authUrl = "http://test401-oauth-se.internal.e-boks.com/1/connect/token",
                kspUrl = "https://demo-m.e-boks.se/app/logon.aspx?logontype=",
                shortAuthId = "MobileApp-Short-id",
                shortAuthSecret = "MobileApp-Short-secret",
                longAuthId = "MobileApp-Long-id",
                longAuthSecret = "MobileApp-Long-secret",
                shortAuthCustomId = "MobileApp-Short-Custom-id",
                shortAuthCustomSecret = "MobileApp-Short-Custom-secret",
                longAuthCustomId = "MobileApp-Long-Custom-id",
                longAuthCustomSecret = "MobileApp-Long-Custom-secret"
            ),
            "demo" to Environments(
                apiUrl = "https://demo-mobile-api-se.internal.e-boks.com/2/",
                authUrl = "https://demo-oauth-se.internal.e-boks.com/1/connect/token",
                kspUrl = "https://demo-m.e-boks.se/app/logon.aspx?logontype=",
                shortAuthId = "MobileApp-Short-id",
                shortAuthSecret = "MobileApp-Short-secret",
                longAuthId = "MobileApp-Long-id",
                longAuthSecret = "MobileApp-Long-secret",
                shortAuthCustomId = "MobileApp-Short-Custom-id",
                shortAuthCustomSecret = "MobileApp-Short-Custom-secret",
                longAuthCustomId = "MobileApp-Long-Custom-id",
                longAuthCustomSecret = "MobileApp-Long-Custom-secret"
            ),
            "production" to Environments(
                apiUrl = "https://mobile-api-se.e-boks.com/2/",
                authUrl = "https://oauth-se.e-boks.com/1/connect/token",
                kspUrl = "https://m.e-boks.se/app/logon.aspx?logontype=",
                shortAuthId = "MobileApp-Short-id",
                shortAuthSecret = "Buz3YmYmjhDRM9R3",
                longAuthId = "MobileApp-Long-id",
                longAuthSecret = "TgtjcNpY9R9ffw8D",
                shortAuthCustomId = "MobileApp-Short-Custom-id",
                shortAuthCustomSecret = "QmaENW6MeYwwjzF5",
                longAuthCustomId = "MobileApp-Long-Custom-id",
                longAuthCustomSecret = "4ZLmEL2SY69MqGKs"
            )
        ),

        customTranslationUrl = "https://m.e-boks.se/app/resources/android/eboks.android.$VERSION.json",
        alternativeLoginProviders = listOf("bankid_se")
    )

    override var resourceLinks: List<ResourceLink> = listOf()
    override val loginProviders: Map<String, LoginProvider> = mapOf(
        "email" to LoginProvider(
            id = "email",
            name = "Email",
            onlyVerified = false,
            icon = -1,
            description = null,
            fragmentClass = LoginComponentFragment::class.java,
            fallbackProvider = "email"
        ),
        "cpr" to LoginProvider(
            id = "cpr",
            name = "Social Security Number",
            onlyVerified = false,
            icon = -1,
            description = null,
            fragmentClass = LoginComponentFragment::class.java,
            fallbackProvider = "cpr"
        ),
        "nemid" to LoginProvider(
            id = "nemid",
            name = "NemID",
            onlyVerified = true,
            icon = R.drawable.black,
            description = Translation.loginproviders.nemidDescription,
            fragmentClass = NemIdComponentFragment::class.java,
            fallbackProvider = "cpr"
        ),
        "idporten" to LoginProvider(
            id = "idporten",
            name = "ID-Porten",
            onlyVerified = true,
            icon = R.drawable.ic_idporten,
            description = Translation.loginproviders.idPortenDescription,
            fragmentClass = IdPortenComponentFragment::class.java,
            fallbackProvider = "cpr"
        ),
        "bankid_se" to LoginProvider(
            id = "bankid_se",
            name = "Bank ID",
            onlyVerified = true,
            icon = R.drawable.ic_bankid,
            description = Translation.loginproviders.bankSeDescription,
            fragmentClass = BankIdSEComponentFragment::class.java,
            fallbackProvider = "cpr"
        ),
        "bankid_no" to LoginProvider(
            id = "bankid_no",
            name = "Bank ID",
            onlyVerified = true,
            icon = R.drawable.ic_bankid_no,
            description = Translation.loginproviders.bankNoDescription,
            fragmentClass = BankIdNOComponentFragment::class.java,
            fallbackProvider = "cpr"
        )
    )

    override fun getLoginProvider(id: String): LoginProvider? {
        return loginProviders[id]
    }

    override val alternativeLoginProviders: List<LoginProvider>
        get() {
            val providers: MutableList<LoginProvider> = ArrayList()
            for (provider_id in currentMode.alternativeLoginProviders) {
                getLoginProvider(provider_id)?.let { provider ->
                    providers.add(provider)
                }
            }
            return providers
        }

    // returns the correct provider id for profile verification depending on country edition
    override val verificationProviderId: String?
        get() {
            return when (currentMode) {
                danish -> "nemid"
                swedish -> "bankid_se"
                norwegian -> "idporten"
                else -> null
            }
        }

    override val isDK: Boolean
        get() {
            return currentMode == danish
        }

    override val isNO: Boolean
        get() {
            return currentMode == norwegian
        }

    override val isSE: Boolean
        get() {
            return currentMode == swedish
        }

    override val currentNationality: String
        get() {
            return when (currentMode) {
                danish -> "DK"
                swedish -> "SE"
                norwegian -> "NO"
                else -> "EN"
            }
        }

    override fun changeConfig(name: String) {
        currentMode = when (name) {
            "danish" -> AppConfigImpl.danish
            "swedish" -> AppConfigImpl.swedish
            "norwegian" -> AppConfigImpl.norwegian
            else -> throw(IllegalStateException("Configuration mode $name is invalid. Use danish, swedish or norwegian"))
        }
    }

    override fun changeEnvironment(name: String) {
        if (!currentMode.environments.containsKey(name)) {
            throw(IllegalStateException("Environment couldn't be changed to $name because it doesn't exist"))
        }
        currentMode.environment = currentMode.environments[name]
    }

    override val isDebug: Boolean
        get() = BuildConfig.BUILD_TYPE.contains("debug", ignoreCase = true)

    override val currentConfigName: String
        get() {
            return when (currentMode) {
                danish -> {
                    "danish"
                }
                swedish -> {
                    "swedish"
                }
                norwegian -> {
                    "norwegian"
                }
                else -> "unknown"
            }
        }

    override val currentEnvironmentName: String?
        get() {
            Timber.e("Running GetCurrentEnvironmentName")
            try {
                for (env in currentMode.environments) {
                    if (env.value == currentMode.environment)
                        return env.key
                }
            } catch (t: Throwable) {
            }
            return null
        }

    override val logoResourceId: Int
        get() {
            return when (currentMode) {
                danish -> {
                    R.drawable.eboks_dk_logo
                }
                swedish -> {
                    R.drawable.eboks_se_logo
                }
                norwegian -> {
                    R.drawable.eboks_no_logo
                }
                else -> R.drawable.eboks_dk_logo
            }
        }

    override val messagePageSize: Int
        get() {
            return 10
        }

    override fun getResourceLinkByType(type: String): ResourceLink? {
        return resourceLinks.find { it.type == type }
    }

    override var currentMode: Mode = when (BuildConfig.mode) {
        "danish" -> danish
        "swedish" -> swedish
        "norwegian" -> norwegian
        else -> throw(IllegalStateException("Configuration mode ${BuildConfig.mode} is invalid. Use danish, swedish or norwegian"))
    }

    init {
        Timber.e("Running AppConfigImpl Init")
        currentMode.environment = if (BuildConfig.BUILD_TYPE.contains("debug", ignoreCase = true))
            currentMode.environments["demo"]
        else
            currentMode.environments["production"]
        if (currentMode == norwegian && BuildConfig.BUILD_TYPE.contains("debug", ignoreCase = true))
            currentMode.environment = currentMode.environments["demo2"]
        if (currentMode.environment == null)
            throw(IllegalStateException("currentMode.environment is not set!!"))
    }
}