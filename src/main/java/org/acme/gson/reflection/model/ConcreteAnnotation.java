package org.acme.gson.reflection.model;

public class ConcreteAnnotation extends Annotation<ConcreteAnnotationType> {
    private String text;

    @Override
    public ConcreteAnnotationType getType() {
        return new ConcreteAnnotationType();
    }

    public ConcreteAnnotation(String text, String value) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }


}
