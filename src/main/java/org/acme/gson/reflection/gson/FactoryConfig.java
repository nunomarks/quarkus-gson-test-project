package org.acme.gson.reflection.gson;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FactoryConfig<T> {
    private static final String PKG = "org.acme.gson.reflection.model";

    private final Class<T> baseClass;

    public FactoryConfig(final Class<T> baseClass) {
        this.baseClass = baseClass;
    }


    @SuppressWarnings("unchecked")
    public RuntimeTypeAdapterFactory<T> configFactory(final String classField) {
        // Name of the super class
        final String clsName = baseClass.getName();
        log.debug("Started creating a RuntimeTypeAdapterFactory for {}.", clsName);

        final RuntimeTypeAdapterFactory<T> factory = RuntimeTypeAdapterFactory.of(baseClass, classField);

        // Obtain all subclasses of the super class
        final Map<String, Class<?>> subClasses = loadSubClasses(clsName);

        // Register all subclasses of the super class
        for (Entry<String, Class<?>> entry : subClasses.entrySet()) {
            factory.registerSubtype((Class<? extends T>) entry.getValue(), entry.getKey());
            log.debug("Registered {} as subtype.", entry.getKey());
        }

        return factory;
    }


    /**
     * Scans and loads the subclasses from a super class from a specific package.
     *
     * @param superClassName String that contains the super class simple name.
     * @return Map with the subclasses from a given super class (key: ub class simple name, value: class)
     */
    private Map<String, Class<?>> loadSubClasses(final String superClassName) {
        log.debug("Started scanning sub classes for class {}.", superClassName);

        final Map<String, Class<?>> subClasses = new LinkedHashMap<>();

        try (ScanResult scanResult = new ClassGraph()
            .enableClassInfo()
            .whitelistPackages(PKG)
            .scan()) {
            final ClassInfoList classes = scanResult.getSubclasses(superClassName);
            for (ClassInfo classInfo : classes) {
                final Class<?> type = classInfo.loadClass();
                final String label = classInfo.getSimpleName();
                log.debug("Class {} was scanned: simple name = {} and loadClass() = {}.", classInfo, label, type);

                subClasses.put(label, type);
            }
        }
        log.debug("Ended scanning classes.");
        return subClasses;
    }
}
