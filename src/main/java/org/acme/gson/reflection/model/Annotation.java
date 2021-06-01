package org.acme.gson.reflection.model;

public abstract class Annotation<T extends AnnotationType> {

    public abstract T getType();
}
