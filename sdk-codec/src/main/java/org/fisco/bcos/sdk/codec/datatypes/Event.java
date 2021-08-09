package org.fisco.bcos.sdk.codec.datatypes;

import org.fisco.bcos.sdk.codec.Utils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Event wrapper type.
 */
public class Event {
    private String name;
    private List<TypeReference<Type>> parameters;

    public Event(String name, List<TypeReference<?>> parameters) {
        this.name = name;
        this.parameters = Utils.convert(parameters);
    }

    public String getName() {
        return this.name;
    }

    public List<TypeReference<Type>> getParameters() {
        return this.parameters;
    }

    public List<TypeReference<Type>> getIndexedParameters() {
        return this.parameters.stream().filter(TypeReference::isIndexed).collect(Collectors.toList());
    }

    public List<TypeReference<Type>> getNonIndexedParameters() {
        return this.parameters.stream().filter(p -> !p.isIndexed()).collect(Collectors.toList());
    }
}
