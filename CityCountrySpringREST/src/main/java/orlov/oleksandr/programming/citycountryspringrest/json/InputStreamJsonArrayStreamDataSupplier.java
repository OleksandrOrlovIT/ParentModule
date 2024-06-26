package orlov.oleksandr.programming.citycountryspringrest.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Supplier for providing a stream of objects from a JSON array in an input stream.
 *
 * @param <T> Type of objects in the stream
 */
@Slf4j
public class InputStreamJsonArrayStreamDataSupplier<T> implements Supplier<Stream<T>> {

    private ObjectMapper mapper = new ObjectMapper();
    private JsonParser jsonParser;
    private Class<T> type;
    private InputStream data;

    /**
     * Constructs an InputStreamJsonArrayStreamDataSupplier.
     *
     * @param type Type of objects in the stream
     * @param data Input stream containing JSON array
     * @throws IOException if an I/O error occurs
     */
    public InputStreamJsonArrayStreamDataSupplier(Class<T> type, InputStream data) throws IOException {
        this.type = type;
        this.data = data;

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);

        jsonParser = mapper.getFactory().createParser(data);
        jsonParser.setCodec(mapper);
        JsonToken token = jsonParser.nextToken();
        if (JsonToken.START_ARRAY.equals(token)) {
            token = jsonParser.nextToken();
        }
        if (!JsonToken.START_OBJECT.equals(token)) {
            log.warn("Can't get any JSON object from input " + data);
        }
    }

    /**
     * Provides a stream of objects parsed from the JSON array in the input stream.
     *
     * @return Stream of objects
     */
    @Override
    public Stream<T> get() {
        return StreamSupport.stream(
                        new Spliterators.AbstractSpliterator<T>(Long.MAX_VALUE, Spliterator.ORDERED) {
                            @Override
                            public boolean tryAdvance(Consumer<? super T> action) {
                                try {
                                    while (jsonParser.nextToken() != null) {
                                        try {
                                            if (jsonParser.getCurrentToken() == JsonToken.END_ARRAY) {
                                                break;
                                            }
                                            T object = jsonParser.readValueAs(type);
                                            action.accept(object);
                                        } catch (IOException e){
                                            log.error(e.getMessage());
                                            jsonParser.nextToken();
                                        }
                                    }
                                    return false;
                                } catch (IOException e) {
                                    log.error(e.getMessage());
                                    return true;
                                }
                            }
                        }, false)
                .onClose(() -> {
                    try {
                        data.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

}