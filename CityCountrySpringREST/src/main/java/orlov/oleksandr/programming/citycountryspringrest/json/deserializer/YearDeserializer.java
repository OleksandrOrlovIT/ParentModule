package orlov.oleksandr.programming.citycountryspringrest.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Year;

/**
 * Custom deserializer for deserializing JSON strings into Year objects.
 */
public class YearDeserializer extends JsonDeserializer<Year> {

    /**
     * Deserializes a JSON string into a Year object.
     *
     * @param jsonParser           JSON parser
     * @param deserializationContext deserialization context
     * @return Year object
     * @throws IOException if an I/O error occurs during deserialization
     */
    @Override
    public Year deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String year = jsonParser.getText();
        return Year.parse(year);
    }
}
