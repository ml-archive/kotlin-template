package dk.eboks.app.domain.config

import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.presentation.ui.components.start.login.LoginComponentFragment
import dk.eboks.app.presentation.ui.components.start.login.providers.nemid.NemIdComponentFragment

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
    private val danish : Mode = Mode(
            urlPrefix = "DA-DK",
            countryCode = "DK",
            cprLength = 10,
            demoVideo = "https://youtu.be/8OmF6uHxfWU",

            integration = Environments(
                baseUrl = "https://integration-rest.e-boks.dk/mobile/1/xml.svc",
                signupUrl = "http://integration-www.e-boks.dk/go?linkid=10101",
                termsUrl = "https://integration-m.e-boks.dk/app/termsofuse.aspx",
                privacyUrl = "https://integration-m.e-boks.dk/app/privacy.aspx",
                helpUrl = "https://integration-m.e-boks.dk/app/help.aspx",
                logonUrl = "https://integration-m.e-boks.dk/app/logon.aspx?logontype="
            ),
            demo2 = Environments(
                baseUrl = "https://demo2-rest.e-boks.dk/mobile/1/xml.svc",
                signupUrl = "http://demo2-www.e-boks.dk/go?linkid=10101",
                termsUrl = "https://demo2-m.e-boks.dk/app/termsofuse.aspx",
                privacyUrl = "https://demo2-m.e-boks.dk/app/privacy.aspx",
                helpUrl = "https://demo2-m.e-boks.dk/app/help.aspx",
                logonUrl = "https://demo2-m.e-boks.dk/app/logon.aspx?logontype="
            ),
            demo = Environments(
                baseUrl = "https://demo-rest.e-boks.dk/mobile/1/xml.svc",
                signupUrl = "http://demo-www.e-boks.dk/go?linkid=10101",
                termsUrl = "https://demo-m.e-boks.dk/app/termsofuse.aspx",
                privacyUrl = "https://demo-m.e-boks.dk/app/privacy.aspx",
                helpUrl = "https://demo-m.e-boks.dk/app/help.aspx",
                logonUrl = "https://demo-m.e-boks.dk/app/logon.aspx?logontype="
            ),
            accept = Environments(
                baseUrl = "https://accept-rest.e-boks.dk/mobile/1/xml.svc",
                signupUrl = "http://accept-www.e-boks.dk/go?linkid=10101",
                termsUrl = "https://accept-m.e-boks.dk/app/termsofuse.aspx",
                privacyUrl = "https://accept-m.e-boks.dk/app/privacy.aspx",
                helpUrl = "https://accept-m.e-boks.dk/app/help.aspx",
                logonUrl = "https://accept-m.e-boks.dk/app/logon.aspx?logontype="
            ),
            test = Environments(
                baseUrl = "http://test-rest.e-boks.dk/mobile/1/xml.svc",
                signupUrl = "http://test-www.e-boks.dk/go?linkid=10101",
                termsUrl = "http://test-ssl-m.e-boks.dk/app/termsofuse.aspx",
                privacyUrl = "http://test-ssl-m.e-boks.dk/app/privacy.aspx",
                helpUrl = "http://test-ssl-m.e-boks.dk/app/help.aspx",
                logonUrl = "http://test-ssl-m.e-boks.dk/app/logon.aspx?logontype="
            ),
            production = Environments(
                baseUrl = "https://rest.e-boks.dk/mobile/1/xml.svc",
                signupUrl = "http://www.e-boks.dk/go?linkid=10101",
                termsUrl = "https://m.e-boks.dk/app/termsofuse.aspx",
                privacyUrl = "https://m.e-boks.dk/app/privacy.aspx",
                helpUrl = "https://m.e-boks.dk/app/help.aspx",
                logonUrl = "https://m.e-boks.dk/app/logon.aspx?logontype="
            ),
            customTranslationUrl = "https://m.e-boks.dk/app/resources/android/eboks.android.3.3.0.json"
    )

    private val norwegian : Mode = Mode(
            urlPrefix = "NB-NO",
            countryCode = "NO",
            cprLength = 11,
            demoVideo = "https://youtu.be/8OmF6uHxfWU",


            integration = Environments(
                    baseUrl = "https://integration-rest.e-boks.dk/mobile/1/xml.svc",
                    signupUrl = "http://integration-www.e-boks.dk/go?linkid=10101",
                    termsUrl = "https://integration-m.e-boks.dk/app/termsofuse.aspx",
                    privacyUrl = "https://integration-m.e-boks.dk/app/privacy.aspx",
                    helpUrl = "https://integration-m.e-boks.dk/app/help.aspx",
                    logonUrl = "https://integration-m.e-boks.dk/app/logon.aspx?logontype="
            ),
            demo2 = Environments(
                    baseUrl = "https://demo2-rest.e-boks.dk/mobile/1/xml.svc",
                    signupUrl = "http://demo2-www.e-boks.dk/go?linkid=10101",
                    termsUrl = "https://demo2-m.e-boks.dk/app/termsofuse.aspx",
                    privacyUrl = "https://demo2-m.e-boks.dk/app/privacy.aspx",
                    helpUrl = "https://demo2-m.e-boks.dk/app/help.aspx",
                    logonUrl = "https://demo2-m.e-boks.dk/app/logon.aspx?logontype="
            ),
            demo = Environments(
                    baseUrl = "https://demo-rest.e-boks.dk/mobile/1/xml.svc",
                    signupUrl = "http://demo-www.e-boks.dk/go?linkid=10101",
                    termsUrl = "https://demo-m.e-boks.dk/app/termsofuse.aspx",
                    privacyUrl = "https://demo-m.e-boks.dk/app/privacy.aspx",
                    helpUrl = "https://demo-m.e-boks.dk/app/help.aspx",
                    logonUrl = "https://demo-m.e-boks.dk/app/logon.aspx?logontype="
            ),
            accept = Environments(
                    baseUrl = "https://accept-rest.e-boks.dk/mobile/1/xml.svc",
                    signupUrl = "http://accept-www.e-boks.dk/go?linkid=10101",
                    termsUrl = "https://accept-m.e-boks.dk/app/termsofuse.aspx",
                    privacyUrl = "https://accept-m.e-boks.dk/app/privacy.aspx",
                    helpUrl = "https://accept-m.e-boks.dk/app/help.aspx",
                    logonUrl = "https://accept-m.e-boks.dk/app/logon.aspx?logontype="
            ),
            test = Environments(
                    baseUrl = "http://test-rest.e-boks.dk/mobile/1/xml.svc",
                    signupUrl = "http://test-www.e-boks.dk/go?linkid=10101",
                    termsUrl = "http://test-ssl-m.e-boks.dk/app/termsofuse.aspx",
                    privacyUrl = "http://test-ssl-m.e-boks.dk/app/privacy.aspx",
                    helpUrl = "http://test-ssl-m.e-boks.dk/app/help.aspx",
                    logonUrl = "http://test-ssl-m.e-boks.dk/app/logon.aspx?logontype="
            ),
            production = Environments(
                    baseUrl = "https://rest.e-boks.dk/mobile/1/xml.svc",
                    signupUrl = "http://www.e-boks.dk/go?linkid=10101",
                    termsUrl = "https://m.e-boks.dk/app/termsofuse.aspx",
                    privacyUrl = "https://m.e-boks.dk/app/privacy.aspx",
                    helpUrl = "https://m.e-boks.dk/app/help.aspx",
                    logonUrl = "https://m.e-boks.dk/app/logon.aspx?logontype="
            ),
            customTranslationUrl = "https://m.e-boks.no/app/resources/android/eboks.android.3.5.0.json"
    )

    private val swedish : Mode = Mode(
            urlPrefix = "SV-SE",
            countryCode = "SE",
            cprLength = 12,
            demoVideo = "https://youtu.be/8OmF6uHxfWU",

            integration = Environments(
                    baseUrl = "https://integration-rest.e-boks.dk/mobile/1/xml.svc",
                    signupUrl = "http://integration-www.e-boks.dk/go?linkid=10101",
                    termsUrl = "https://integration-m.e-boks.dk/app/termsofuse.aspx",
                    privacyUrl = "https://integration-m.e-boks.dk/app/privacy.aspx",
                    helpUrl = "https://integration-m.e-boks.dk/app/help.aspx",
                    logonUrl = "https://integration-m.e-boks.dk/app/logon.aspx?logontype="
            ),
            demo2 = Environments(
                    baseUrl = "https://demo2-rest.e-boks.dk/mobile/1/xml.svc",
                    signupUrl = "http://demo2-www.e-boks.dk/go?linkid=10101",
                    termsUrl = "https://demo2-m.e-boks.dk/app/termsofuse.aspx",
                    privacyUrl = "https://demo2-m.e-boks.dk/app/privacy.aspx",
                    helpUrl = "https://demo2-m.e-boks.dk/app/help.aspx",
                    logonUrl = "https://demo2-m.e-boks.dk/app/logon.aspx?logontype="
            ),
            demo = Environments(
                    baseUrl = "https://demo-rest.e-boks.dk/mobile/1/xml.svc",
                    signupUrl = "http://demo-www.e-boks.dk/go?linkid=10101",
                    termsUrl = "https://demo-m.e-boks.dk/app/termsofuse.aspx",
                    privacyUrl = "https://demo-m.e-boks.dk/app/privacy.aspx",
                    helpUrl = "https://demo-m.e-boks.dk/app/help.aspx",
                    logonUrl = "https://demo-m.e-boks.dk/app/logon.aspx?logontype="
            ),
            accept = Environments(
                    baseUrl = "https://accept-rest.e-boks.dk/mobile/1/xml.svc",
                    signupUrl = "http://accept-www.e-boks.dk/go?linkid=10101",
                    termsUrl = "https://accept-m.e-boks.dk/app/termsofuse.aspx",
                    privacyUrl = "https://accept-m.e-boks.dk/app/privacy.aspx",
                    helpUrl = "https://accept-m.e-boks.dk/app/help.aspx",
                    logonUrl = "https://accept-m.e-boks.dk/app/logon.aspx?logontype="
            ),
            test = Environments(
                    baseUrl = "http://test-rest.e-boks.dk/mobile/1/xml.svc",
                    signupUrl = "http://test-www.e-boks.dk/go?linkid=10101",
                    termsUrl = "http://test-ssl-m.e-boks.dk/app/termsofuse.aspx",
                    privacyUrl = "http://test-ssl-m.e-boks.dk/app/privacy.aspx",
                    helpUrl = "http://test-ssl-m.e-boks.dk/app/help.aspx",
                    logonUrl = "http://test-ssl-m.e-boks.dk/app/logon.aspx?logontype="
            ),
            production = Environments(
                    baseUrl = "https://rest.e-boks.dk/mobile/1/xml.svc",
                    signupUrl = "http://www.e-boks.dk/go?linkid=10101",
                    termsUrl = "https://m.e-boks.dk/app/termsofuse.aspx",
                    privacyUrl = "https://m.e-boks.dk/app/privacy.aspx",
                    helpUrl = "https://m.e-boks.dk/app/help.aspx",
                    logonUrl = "https://m.e-boks.dk/app/logon.aspx?logontype="
            ),
            customTranslationUrl = "https://m.e-boks.se/app/resources/android/eboks.android.3.5.0.json"
    )

    val loginProviders: Map<String, LoginProvider> = mapOf(
            "email" to LoginProvider("email", "Email", false, -1, null, LoginComponentFragment::class.java),
            "cpr" to LoginProvider("cpr", "Social Security Number", false, -1, null, LoginComponentFragment::class.java),
            "nemid" to LoginProvider("nemid", "NemID", true, -1, null, NemIdComponentFragment::class.java),
            "idporten" to LoginProvider("idporten", "ID-Porten", true, R.drawable.ic_idporten, "_Use this to see mail from public authorities", NemIdComponentFragment::class.java),
            "bankid_se" to LoginProvider("bankid_se", "Bank ID", true, R.drawable.ic_bankid, null, NemIdComponentFragment::class.java),
            "bankid_no" to LoginProvider("bankid_no", "Bank ID", true, R.drawable.ic_bankid, null, NemIdComponentFragment::class.java)
    )

    fun getLoginProvider(id : String) : LoginProvider?
    {
        return loginProviders[id]
    }

    fun isDK() : Boolean { return BuildConfig.mode == "danish" }
    fun isNO() : Boolean { return BuildConfig.mode == "norwegian" }
    fun isSE() : Boolean { return BuildConfig.mode == "swedish" }

    lateinit var currentMode : Mode

    init {
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
        currentMode.environment = if(BuildConfig.DEBUG) currentMode.demo else currentMode.production
        if(currentMode.environment == null)
            throw(IllegalStateException("currentMode.environment is not set!!"))
    }
}