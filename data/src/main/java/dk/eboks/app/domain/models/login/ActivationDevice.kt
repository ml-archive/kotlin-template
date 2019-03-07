package dk.eboks.app.domain.models.login

import com.google.gson.annotations.SerializedName

data class ActivationDevice(
    @SerializedName("id") var deviceId: String,
    @SerializedName("name") var deviceName: String,
    @SerializedName("os") var deviceOs: String,
    @SerializedName("key") var key: String
)