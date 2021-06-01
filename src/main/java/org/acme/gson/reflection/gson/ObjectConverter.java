package org.acme.gson.reflection.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;
import java.util.List;
import org.acme.gson.reflection.model.Annotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ObjectConverter {
    private static final Logger log = LoggerFactory.getLogger(ObjectConverter.class);
    private static final Gson gson = createGsonBuilder().create();

    private ObjectConverter() {
    }

    public static Gson getGson() {
        return gson;
    }

    private static GsonBuilder createGsonBuilder() {
        log.debug("Creating Gson Builder...");
        GsonBuilder gsonBuilder = (new GsonBuilder()).registerTypeHierarchyAdapter(Class.class, new ClassSerializer("ot"));
        return (new GsonBuilder()).registerTypeHierarchyAdapter(Object.class, new ObjectSerializer(gsonBuilder, "ot"))
            .registerTypeHierarchyAdapter(Class.class, new ClassDeserializer()).registerTypeAdapterFactory((new FactoryConfig(
            Annotation.class)).configFactory("ot"));
    }

    public static String objectToJson(Object object) {
        return gson.toJson(object);
    }

    public static Object jsonToObject(String json, Class<?> type) {
        return gson.fromJson(json, type);
    }

    public static List jsonToObject(String json, Type type) {
        return (List)gson.fromJson(json, type);
    }
}

