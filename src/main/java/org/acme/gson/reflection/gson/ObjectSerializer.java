package org.acme.gson.reflection.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectSerializer implements JsonSerializer<Object> {
    private final Gson gson;
    private final String typeFieldName;

    public ObjectSerializer(final GsonBuilder gsonBuilder, final String typeFieldName) {
        gson = gsonBuilder.create();
        this.typeFieldName = typeFieldName;
    }


    public JsonObject serializeClassFields(final Field[] fields, final JsonObject jsonObject, final Object src, final Type typeOfSrc, final JsonSerializationContext context) {
        // Iterate over the class fields to serialize each one of them
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            try {
                final SerializedName serializedName = field.getAnnotation(SerializedName.class);

                // In order to avoid duplicated fields, verifies if the current field is annotated with @SerializedName annotation
                if (serializedName != null) {
                    fieldName = serializedName.value();
                }
                if (field.get(src) != null) {
                    // Serialize the field and add it to the JSON object
                    jsonObject.add(fieldName, context.serialize(field.get(src), field.getType()));
                }
            } catch (IllegalAccessException e) {
                log.warn("Illegal access to field \"{}\" from class \"{}\".", fieldName, typeOfSrc.getTypeName(), e);
            }
        }
        return jsonObject;
    }


    @Override
    @SuppressWarnings("unchecked")
    public JsonElement serialize(final Object src, final Type typeOfSrc, final JsonSerializationContext context) {
        final JsonElement serialize = gson.toJsonTree(src);

        if (serialize.isJsonObject()) {

            // Verify if is a Class
            if (!typeOfSrc.getTypeName().equals(Class.class.getTypeName())) {
                JsonObject contentObj = serialize.getAsJsonObject();
                // Add the class simple name to the JSON object
                contentObj.addProperty(typeFieldName, src.getClass().getSimpleName());

                // Serialize the declared fields from the class
                contentObj = serializeClassFields(src.getClass().getDeclaredFields(), contentObj, src, typeOfSrc, context);

                // Serialize the declared fields from the super classes
                Class<?> superClass = src.getClass().getSuperclass();
                while (superClass != Object.class) {
                    contentObj = serializeClassFields(superClass.getDeclaredFields(), contentObj, src, typeOfSrc, context);
                    superClass = superClass.getSuperclass();
                }

                return contentObj;
            }
        } else if (serialize.isJsonArray()) {
            final JsonArray array = new JsonArray();

            // Verify if is Array or List type
            if (src.getClass().isArray()) {
                log.debug("It is Array type.");
                final Object[] listSrcArr = (Object[]) src;

                for (Object srcElement : listSrcArr) {
                    // Serialize the array element and add it to the JsonArray
                    array.add(context.serialize(srcElement));
                }
            } else {
                log.debug("It is List type.");
                final List<Object> listSrc = (List<Object>) src;

                for (Object srcElement : listSrc) {
                    // Serialize the list element and add it to the JsonArray
                    array.add(context.serialize(srcElement));
                }
            }
            return array;
        }
        log.debug("It is JsonNull or JsonPrimitive.");
        return serialize;
    }
}
