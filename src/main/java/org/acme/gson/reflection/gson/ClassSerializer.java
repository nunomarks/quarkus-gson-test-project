package org.acme.gson.reflection.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClassSerializer implements JsonSerializer<Class> {
    private final String typeFieldName;

    public ClassSerializer(final String typeFieldName) {
        this.typeFieldName = typeFieldName;
    }

    @Override
    public JsonElement serialize(final Class aClass, final Type type, final JsonSerializationContext jsonSerializationContext) {
        final String classSimpleName = aClass.getSimpleName();
        log.debug("Serializing Class {}.", classSimpleName);

        final JsonObject obj = new JsonObject();
        obj.addProperty(typeFieldName, classSimpleName);
        return obj;
    }
}
