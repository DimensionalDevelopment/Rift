package org.dimdev.riftloader;

public class MissingDependencyException extends RuntimeException {
    public MissingDependencyException(String s) {
        super(s);
    }
}
