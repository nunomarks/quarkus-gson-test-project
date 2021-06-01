package org.acme.gson.reflection;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.acme.gson.reflection.model.ConcreteAnnotation;
import org.acme.gson.reflection.model.Document;
import org.acme.gson.reflection.gson.ObjectConverter;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

public class ObjectConverterTest {

    @Test
    public void fromJson() throws IOException {
        final String jsonString = FileUtils
            .readFileToString(new File(
                Objects.requireNonNull(ObjectConverterTest.class.getClassLoader().getResource("document.json")).getFile()),
                StandardCharsets.UTF_8);

        final Document document =  (Document) ObjectConverter.jsonToObject(jsonString, Document.class);

        assertEquals("one", ((ConcreteAnnotation)document.getAnnotation()).getText());
    }

}