package dk.eboks.app.domain.models.message.payment

data class PaymentRequest(
    val messageId: String,
    val paymentType: String,
    val folderId: Int,
    val successURL: String = PaymentCallback.SUCCESS.url,
    val failureURL: String = PaymentCallback.FAILURE.url,
    val cancelURL: String = PaymentCallback.CANCEL.url
) {

    companion object
}