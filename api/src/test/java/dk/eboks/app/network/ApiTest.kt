package dk.eboks.app.network

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import dk.eboks.app.domain.models.formreply.ReplyForm
import dk.eboks.app.network.util.DateDeserializer

import dk.eboks.app.network.util.ItemTypeAdapterFactory
import org.junit.Test
import java.util.Date

class ApiTest {

    @Test
    fun `Test deserialize reply form`() {

        val gson = GsonBuilder()
            .registerTypeAdapterFactory(ItemTypeAdapterFactory())
            .registerTypeAdapter(
                java.lang.Double::class.java,
                JsonDeserializer<Double> { json, _, _ ->
                    val string = json?.asString ?: return@JsonDeserializer null
                    try {
                        string.toDouble()
                    } catch (ne: NumberFormatException) {
                        string.replace(",", ".").toDouble()
                    }
                })
            .registerTypeAdapter(Date::class.java, DateDeserializer())
            .setDateFormat(DateDeserializer.DATE_FORMATS[0])
            .create()

        gson.fromJson(getMessageReplyFormJsonString, ReplyForm::class.java)
    }
}