package dk.eboks.app.domain.config

/**
 * Created by joso on 11/08/15.
 */
data class Environments (
    var demo : String = "https://demo-rest.e-boks.dk/mobile/1/xml.svc",
    var demo2 : String = "https://demo-m.e-boks.dk/ref/1/",
    var accept : String = "https://demo-m.e-boks.dk/ref/1/",
    var production : String = "https://demo-m.e-boks.dk/ref/1/",
    var baseUrl : String = "https://demo-rest.e-boks.dk/mobile/1/xml.svc",
    var signupUrl : String = "http://demo-www.e-boks.dk/go?linkid=10101",
    var termsUrl : String = "https://m.e-boks.se/app/termsofuse.aspx?culture=",
    var privacyUrl : String = "https://demo-m.e-boks.dk/app/privacy.aspx",
    var helpUrl : String = "https://m.e-boks.se/app/help.aspx?culture=",
    var logonUrl : String = "",
    var bankIdUrl : String = ""
)
