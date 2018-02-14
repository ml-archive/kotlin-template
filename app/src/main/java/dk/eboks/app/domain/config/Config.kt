package dk.eboks.app.domain.config

import dk.eboks.app.BuildConfig

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
            nemIdEnabled = true,
            feedEnabled = true,
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


    lateinit var currentMode : Mode

    init {
        when(BuildConfig.mode)
        {
            "danish" -> {
                currentMode = danish
            }
            "swedish" -> {

            }
            "norwegian" -> {

            }
            else -> throw(IllegalStateException("Configuration mode ${BuildConfig.mode} is invalid. Use danish, swedish or norwegian"))
        }
        currentMode.environment = if(BuildConfig.DEBUG) currentMode.demo else currentMode.production
        if(currentMode.environment == null)
            throw(IllegalStateException("currentMode.environment is not set!!"))
    }
}