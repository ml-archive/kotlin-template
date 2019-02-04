package dk.eboks.app.domain.models.channel.storebox

import com.google.gson.annotations.SerializedName

/**
"cardId": "4zaagt7b00zketcuyjmn6jka6o7qnpqi",
"maskedCardNumber": "460834XXXXXX0726",
"expiryMonth": 10,
"expiryYear": 19
 */
data class StoreboxCreditCard(
    @SerializedName("cardId")
    val id: String = "",
    val maskedCardNumber: String = "",
    val expiryMonth: Int = 0,
    val expiryYear: Int = 0
)