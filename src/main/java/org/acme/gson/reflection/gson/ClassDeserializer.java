package org.acme.gson.reflection.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClassDeserializer implements JsonDeserializer<Class> {
    private static final String PKG = "org.acme.gson.reflection.model";

    private final Map<String, Class<?>> pkgClasses = new LinkedHashMap<>();


    public ClassDeserializer() {
        // Load the classes from a specific package
        loadClasses(PKG);
    }


    @Override
    public Class deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final String classSimpleName = jsonElement.getAsJsonObject().get("ot").getAsString();
        log.debug("Deserialize class [{}].", classSimpleName);

        // Return the Class corresponding to the simple name from the JsonElement
        return pkgClasses.get(classSimpleName);
    }


    /**
     * Scans and loads the classes from a specific package.
     *
     * @param pkg String that contains the package to scan the classes.
     */
    public void loadClasses(final String pkg) {
        log.debug("Started scanning classes.");

        try (ScanResult scanResult = new ClassGraph()
            .enableClassInfo()
            .whitelistPackages(pkg)
            .scan()) {
            final ClassInfoList classes = scanResult.getAllClasses();

            for (ClassInfo classInfo : classes) {
                final String label = classInfo.getSimpleName();
                final Class<?> type = classInfo.loadClass();
               // log.debug("Class [{}] was scanned: simple name = [{}] and loadClass() = [{}].", classInfo, classInfo.getSimpleName(), classInfo.loadClass());

                pkgClasses.put(label, type);
                log.debug("Registered [{}] with label \"{}\".", type, label);
            }
        }
        log.debug("Ended scanning classes.");
    }
}
