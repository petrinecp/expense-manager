package cz.muni.expense.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;
import cz.muni.expense.model.BaseEntity;

/**
 * Class for retrieve mappings from json to object
 *
 * @author Martin Drimal
 *
 */
public class ObjectParser<T extends BaseEntity> {

    private final ObjectMapper mapper = new ObjectMapper();

    private final Class<T> type;

    public ObjectParser(Class<T> type) {
        this.type = type;
    }

    private Class<T> getType() {
        return this.type;
    }

    public List<T> getObjectFromJson(String jsonContent) throws Exception {
        if (jsonContent == null || jsonContent.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        List<T> objects = new ArrayList<>();

        JsonFactory f = new JsonFactory();
        JsonParser jp = f.createJsonParser(jsonContent);
        jp.nextToken();

        while (jp.hasCurrentToken()) {
            JsonToken currentToken = jp.getCurrentToken();
            if (currentToken == JsonToken.START_OBJECT) {

                T obj = mapper.readValue(jp, getType());
                objects.add(obj);
            }
            jp.nextToken();
        }

        return objects;
    }
}
