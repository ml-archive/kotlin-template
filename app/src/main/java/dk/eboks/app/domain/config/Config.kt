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
import java.net.URL

// TODO this stuff should be downloaded from a url (on request of the customer) so that the app only contains
// one harded coded url ya'll
/**
 * Created by bison on 07/12/17.
 *
 * This class should not be written to at runtime, it presents a basic configuration for each of the
 * app flavors (DK, SE, NO)
 *
 */
object Config {

    var resourceLinks : List<ResourceLink>? = null


    private val danish : Mode = Mode(
            urlPrefix = "DA-DK",
            countryCode = "DK",
            cprLength = 10,
            demoVideo = "https://youtu.be/8OmF6uHxfWU",

            environments = mapOf<String, Environments>(
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

            customTranslationUrl = "https://m.e-boks.dk/app/resources/android/eboks.android.4.0.2.json",
            alternativeLoginProviders = listOf("nemid")
    )

    private val norwegian : Mode = Mode(
            urlPrefix = "NB-NO",
            countryCode = "NO",
            cprLength = 11,
            demoVideo = "https://youtu.be/8OmF6uHxfWU",


            environments = mapOf<String, Environments>(
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

            customTranslationUrl = "https://m.e-boks.no/app/resources/android/eboks.android.4.0.2.json",
            alternativeLoginProviders = listOf("idporten", "bankid_no")
    )

    private val swedish : Mode = Mode(
            urlPrefix = "SV-SE",
            countryCode = "SE",
            cprLength = 12,
            demoVideo = "https://youtu.be/8OmF6uHxfWU",

            environments = mapOf<String, Environments>(
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

            customTranslationUrl = "https://m.e-boks.se/app/resources/android/eboks.android.4.0.2.json",
            alternativeLoginProviders = listOf("bankid_se")
    )

    val loginProviders: Map<String, LoginProvider> = mapOf(
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
                    icon = R.drawable.ic_bankid,
                    description = Translation.loginproviders.bankNoDescription,
                    fragmentClass = BankIdNOComponentFragment::class.java,
                    fallbackProvider = "cpr"
            )
    )

    fun getLoginProvider(id : String) : LoginProvider?
    {
        return loginProviders[id]
    }

    fun getAlternativeLoginProviders() : MutableList<LoginProvider>
    {
        val providers : MutableList<LoginProvider> = ArrayList()
        for(provider_id in currentMode.alternativeLoginProviders)
        {
            getLoginProvider(provider_id)?.let { provider ->
                providers.add(provider)
            }
        }
        return providers
    }

    // returns the correct provider id for profile verification depending on country edition
    fun getVerificationProviderId() : String?
    {
        return when(currentMode)
        {
            danish -> "nemid"
            swedish -> "bankid_se"
            norwegian -> "idporten"
            else -> null
        }
    }

    fun isDK() : Boolean { return currentMode == danish }
    fun isNO() : Boolean { return currentMode == norwegian }
    fun isSE() : Boolean { return currentMode == swedish }

    fun getCurrentNationality() : String
    {
        return when(currentMode)
        {
            danish -> "DK"
            swedish -> "SE"
            norwegian -> "NO"
            else -> "EN"
        }
    }

    fun changeConfig(name : String)
    {
        when(name)
        {
            "danish" -> {
                Config.currentMode = Config.danish
            }
            "swedish" -> {
                Config.currentMode = Config.swedish
            }
            "norwegian" -> {
                Config.currentMode = Config.norwegian
            }
            else -> throw(IllegalStateException("Configuration mode ${name} is invalid. Use danish, swedish or norwegian"))
        }
    }

    fun changeEnvironment(name : String)
    {
        if(!currentMode.environments.containsKey(name))
        {
            throw(IllegalStateException("Environment couldn't be changed to $name because it doesn't exist"))
        }
        currentMode.environment = currentMode.environments[name]

    }

    fun getCurrentConfigName() : String
    {
        when(currentMode)
        {
            danish -> {
                return "danish"
            }
            swedish -> {
                return "swedish"
            }
            norwegian -> {
                return "norwegian"
            }
            else -> return "unknown"
        }
    }

    fun getCurrentEnvironmentName() : String?
    {
        Timber.e("Running GetCurrentEnvironmentName")
        try {
            for(env in currentMode.environments)
            {
                if(env.value == currentMode.environment)
                    return env.key
            }
        }
        catch (t : Throwable)
        {
        }
        return null
    }

    fun getLogoResourceId() : Int
    {
        when(currentMode)
        {
            danish -> {
                return R.drawable.eboks_dk_logo
            }
            swedish -> {
                return R.drawable.eboks_se_logo
            }
            norwegian -> {
                return R.drawable.eboks_no_logo
            }
            else -> return R.drawable.eboks_dk_logo
        }
    }

    fun getApiUrl() : String
    {
        return currentMode.environment?.apiUrl ?: throw(IllegalStateException("No api url selected"))
    }

    fun getAuthUrl() : String
    {
        return currentMode.environment?.authUrl ?: throw(IllegalStateException("No auth url selected"))
    }

    fun getApiHost() : String
    {
        return URL(getApiUrl()).host
    }

    fun getApiScheme() : String
    {
        return URL(getApiUrl()).protocol
    }

    fun getAuthHost() : String
    {
        return URL(getApiUrl()).host
    }

    fun getTermsAndConditionsUrl() : String
    {
        return getApiUrl() + "resources/terms"
    }

    fun getMessagePageSize() : Int
    {
        return 10
    }

    fun getResourceLinkByType(type : String) : ResourceLink?
    {
        resourceLinks?.let { links->
            for(link in links)
            {
                if(link.type == type)
                    return link
            }
        }
        return null
    }

    var currentMode : Mode


    init {
        Timber.e("Running Config Init")
        when(BuildConfig.mode)
        {
            "danish" -> {
                currentMode = danish
            }
            "swedish" -> {
                currentMode = swedish
            }
            "norwegian" -> {
                currentMode = norwegian
            }
            else -> throw(IllegalStateException("Configuration mode ${BuildConfig.mode} is invalid. Use danish, swedish or norwegian"))
        }
        currentMode.environment = if(BuildConfig.BUILD_TYPE.contains("debug", ignoreCase = true)) currentMode.environments["demo"] else currentMode.environments["production"]
        if(currentMode == norwegian && BuildConfig.BUILD_TYPE.contains("debug", ignoreCase = true))
            currentMode.environment = currentMode.environments["demo2"]
        if(currentMode.environment == null)
            throw(IllegalStateException("currentMode.environment is not set!!"))

    }
}