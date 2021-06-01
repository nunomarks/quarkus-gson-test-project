package org.acme.gson.reflection;


import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.acme.gson.reflection.model.Document;
import org.acme.gson.reflection.gson.ObjectConverter;
import org.apache.commons.io.FileUtils;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String hello() throws IOException {
        String jsonString;
        jsonString = FileUtils
            .readFileToString(new File(Objects.requireNonNull(GreetingResource.class.getClassLoader().getResource("document.json")).getFile()), StandardCharsets.UTF_8);
        Document document = (Document) ObjectConverter.jsonToObject(jsonString, Document.class);
        return ObjectConverter.objectToJson(document);
    }
}