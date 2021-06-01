package org.acme.gson.reflection.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


class ConcreteAnnotationTest {

    @Test
    public void testNew() {
        final Annotation concreteAnnotation = new ConcreteAnnotation("text1", "value");
        assertEquals("MY_ANNOTATION", concreteAnnotation.getType().getType());
    }

}