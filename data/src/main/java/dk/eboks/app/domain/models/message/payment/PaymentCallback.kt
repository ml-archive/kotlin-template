package dk.eboks.app.domain.models.message.payment

enum class PaymentCallback(val url: String) {
    SUCCESS("eboks://success/"),
    FAILURE("eboks://failure/"),
    CANCEL("eboks://cancel/")
}