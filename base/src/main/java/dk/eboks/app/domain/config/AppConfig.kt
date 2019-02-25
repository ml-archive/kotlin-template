package dk.eboks.app.domain.config

import dk.eboks.app.domain.models.shared.ResourceLink
import java.net.URL

interface AppConfig {
    val loginProviders: Map<String, LoginProvider>
    var resourceLinks: List<ResourceLink>
    fun getLoginProvider(id: String): LoginProvider?
    val alternativeLoginProviders: List<LoginProvider>
    val verificationProviderId: String?
    val isDK: Boolean
    val isNO: Boolean
    val isSE: Boolean
    val currentNationality: String
    fun changeConfig(name: String)
    fun changeEnvironment(name: String)
    val currentConfigName: String
    val currentEnvironmentName: String?
    val logoResourceId: Int
    var currentMode: Mode
    val messagePageSize: Int
    val isDebug: Boolean


    val ENABLE_BETA_DISCLAIMER : Boolean
    val ENABLE_CHANNEL_REORDERING : Boolean
    val ENABLE_DOCUMENT_ACTIONS : Boolean
    val ENABLE_EKEY : Boolean
    val ENABLE_FINGERPRINT_NONVERIFIED : Boolean
    val ENABLE_FOLDERS_ACTIONS : Boolean
    val ENABLE_PAYMENT : Boolean
    val ENABLE_PROFILE_DATA_VERIFICATION : Boolean
    val ENABLE_PROFILE_PICTURE : Boolean
    val ENABLE_REPLY : Boolean
    val ENABLE_SENDERS : Boolean
    val ENABLE_SHARES : Boolean
    val ENABLE_SIGN : Boolean
    val ENABLE_UPLOADS : Boolean

    fun getApiUrl(): String {
        return currentMode.environment?.apiUrl
            ?: throw(IllegalStateException("No api url selected"))
    }

    fun getAuthUrl(): String {
        return currentMode.environment?.authUrl
            ?: throw(IllegalStateException("No auth url selected"))
    }

    fun getApiHost(): String {
        return URL(getApiUrl()).host
    }

    fun getApiScheme(): String {
        return URL(getApiUrl()).protocol
    }

    fun getAuthHost(): String {
        return URL(getApiUrl()).host
    }

    fun getTermsAndConditionsUrl(): String {
        return getApiUrl() + "resources/terms"
    }

    fun getResourceLinkByType(type: String): ResourceLink?
}