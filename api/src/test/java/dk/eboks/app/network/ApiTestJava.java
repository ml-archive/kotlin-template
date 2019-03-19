package dk.eboks.app.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Date;

import dk.eboks.app.domain.models.formreply.ReplyForm;
import dk.eboks.app.network.util.DateDeserializer;
import dk.eboks.app.network.util.ItemTypeAdapterFactory;

public class ApiTestJava {
    @Test
    public void testDeserializeReplyForm() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .registerTypeAdapter(Double.class, new JsonDeserializer<Double>() {
                    @Override
                    public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        try {
                            return json.getAsDouble();
                        } catch (NumberFormatException ne) {
                            return Double.valueOf(json.getAsString().replace(',', '.'));
                        }

                    }
                })
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();

        ReplyForm form = gson.fromJson(getMessageReply, ReplyForm.class);
        form.getInputs();

    }

    private static String getMessageReply = "{\n" +
            "  \"inputs\": [\n" +
            "    {\n" +
            "      \"type\": \"Link\",\n" +
            "      \"label\": \"Læs mere om e-Boks\",\n" +
            "      \"required\": false,\n" +
            "      \"readonly\": false,\n" +
            "      \"value\": \"http://e-boks.dk\",\n" +
            "      \"options\": []\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"Kommentar\",\n" +
            "      \"type\": \"TextArea\",\n" +
            "      \"label\": \"Skriv en kommentar\",\n" +
            "      \"minLength\": \"100\",\n" +
            "      \"required\": false,\n" +
            "      \"readonly\": false,\n" +
            "      \"value\": \"\",\n" +
            "      \"options\": []\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"Kommentar\",\n" +
            "      \"type\": \"Text\",\n" +
            "      \"label\": \"Skriv en kommentar\",\n" +
            "      \"validate\": \"^[_a-z0-9-]+(.[_a-z0-9-]+)*@([_a-z0-9-]+)(.[_a-z0-9-]+)*[.][a-z]{2,}+$\",\n" +
            "      \"minLength\": \"100\",\n" +
            "      \"required\": false,\n" +
            "      \"readonly\": false,\n" +
            "      \"value\": \"\",\n" +
            "      \"options\": []\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"Amount\",\n" +
            "      \"type\": \"Number\",\n" +
            "      \"label\": \"Indtast beløb\",\n" +
            "      \"maxValue\": \"500,00\",\n" +
            "      \"required\": true,\n" +
            "      \"readonly\": false,\n" +
            "      \"value\": \"100\",\n" +
            "      \"options\": []\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"Regioner\",\n" +
            "      \"type\": \"List\",\n" +
            "      \"label\": \"Vælg regioner\",\n" +
            "      \"required\": false,\n" +
            "      \"readonly\": false,\n" +
            "      \"value\": \"region2,region3\",\n" +
            "      \"options\": [\n" +
            "        {\n" +
            "          \"name\": \"Nordjylland\",\n" +
            "          \"value\": \"region1\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"Midtjylland\",\n" +
            "          \"value\": \"region2\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"Syddanmark\",\n" +
            "          \"value\": \"region3\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"Hovedstaden\",\n" +
            "          \"value\": \"region4\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"Sjælland\",\n" +
            "          \"value\": \"region5\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"Region\",\n" +
            "      \"type\": \"Radiobox\",\n" +
            "      \"label\": \"Vælg en region\",\n" +
            "      \"required\": false,\n" +
            "      \"readonly\": false,\n" +
            "      \"value\": \"region3\",\n" +
            "      \"options\": [\n" +
            "        {\n" +
            "          \"name\": \"Nordjylland\",\n" +
            "          \"value\": \"region1\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"Midtjylland\",\n" +
            "          \"value\": \"region2\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"Syddanmark\",\n" +
            "          \"value\": \"region3\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"Hovedstaden\",\n" +
            "          \"value\": \"region4\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"Sjælland\",\n" +
            "          \"value\": \"region5\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"Fødselsdato\",\n" +
            "      \"type\": \"Date\",\n" +
            "      \"label\": \"Indtast fødselsdato\",\n" +
            "      \"required\": true,\n" +
            "      \"readonly\": false,\n" +
            "      \"value\": \"1989-09-25\",\n" +
            "      \"options\": []\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"Udskrivelsestid\",\n" +
            "      \"type\": \"Datetime\",\n" +
            "      \"label\": \"Udskrivelsesdato/tid\",\n" +
            "      \"required\": true,\n" +
            "      \"readonly\": false,\n" +
            "      \"value\": \"2016-09-10T12:00:00\",\n" +
            "      \"options\": []\n" +
            "    }\n" +
            "  ]\n" +
            "}";
}
