package dk.eboks.app.domain.config


/**
 * Created by joso on 10/08/15.
 */
data class Mode (
    var signupEnabled : Boolean = false,
    var environment: Environments? = null,
    var urlPrefix: String = "",
    var countryCode: String = "",
    var cprLength : Int = 10,
    var phoneNumberLength : Int  = 8,
    var demoVideo : String = "https://youtu.be/bTCszFEp4lw",
    var about : String = "http://www.e-boks.se",
    var dateTimeFormat : String = "dd. LLL yyyy hh:mm:ss",
    var dateFormat : String = "d. LLL yyyy",
    var dateFormReplyFormat : String = "yyyy-MM-dd",
    var dateTimeFormReplyFormat : String = "yyyy-MM-dd hh:mm:ss",
    var dateFormMinMaxValueFormat : String = "yyyy-MM-dd",
    var dateSearchFormat : String = "yyyy-MM-dd",
    var paymentDateFormat : String = "MM/dd/yyyy",
    var phoneCountryCode: String = "",
    var demo: Environments? = null,
    var demo2: Environments? = null,
    var accept: Environments? = null,
    var production: Environments? = null,
    var integration: Environments? = null,
    var test: Environments? = null,
    var maxFileUploadSize : Int = 1024 * 1024 * 10,
    var maxFileUploadNameLength : Int = 50,
    var customTranslationUrl : String = ""
)
