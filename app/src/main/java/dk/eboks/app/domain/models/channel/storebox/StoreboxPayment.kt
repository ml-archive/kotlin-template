package dk.eboks.app.domain.models.channel.storebox

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class StoreboxPayment(
        @SerializedName("paymentType")
        var paymentType: String? = null,
        @SerializedName("priceValue")
        var priceValue: String? = null,
        @SerializedName("priceCurrency")
        var priceCurrency: Double? = null,
        @SerializedName("truncatedCardNumber")
        var truncatedCardNumber: String? = null,
        @SerializedName("cardName")
        var cardName: String? = null,
        @SerializedName("foreignCurrencyCode")
        var foreignCurrencyCode: String? = null,
        @SerializedName("foreignCurrencyAmount")
        var foreignCurrencyAmount: Double? = null,
        @SerializedName("foreignCurrencyExchangeRate")
        var foreignCurrencyExchangeRate: Double? = null,
        @SerializedName("foreignCurrencyLabel")
        var foreignCurrencyLabel: String? = null
) : Parcelable