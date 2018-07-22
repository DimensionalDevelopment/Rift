package org.dimdev.accesstransform;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AccessTransformationSet {
    private final Map<ElementReference, AccessLevel> transformations = new HashMap<>();
    private final Set<String> affectedClasses = new HashSet<>();

    public void addMimimumAccessLevel(ElementReference elementReference, AccessLevel accessLevel) {
        transformations.put(elementReference, AccessLevel.union(transformations.get(elementReference), accessLevel));
        affectedClasses.add(elementReference.kind == ElementReference.Kind.CLASS ? elementReference.name : elementReference.owner);
    }

    public void addMinimumAccessLevel(String string) {
        int indexOfFirstSpace = string.indexOf(' ');
        String accessLevel = string.substring(0, indexOfFirstSpace);
        String elementReference = string.substring(indexOfFirstSpace + 1);

        addMimimumAccessLevel(ElementReference.fromString(elementReference), AccessLevel.fromString(accessLevel));
    }

    public AccessLevel getMinimumAccessLevel(ElementReference elementReference) {
        return transformations.get(elementReference);
    }

    public boolean isClassAffected(String name) {
        return affectedClasses.contains(name);
    }

    public boolean isEmpty() {
        return transformations.size() == 0;
    }
}
