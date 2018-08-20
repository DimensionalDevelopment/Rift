package org.dimdev.riftloader.listener;

/**
 * Allows mods to provide ways to create instances of classes which don't
 * have a public no-args constructor.
 * <p>
 * A class implementing the instantiator interface must necessarily contain
 * a public no-args constructor, to prevent infinite recursion while attempting
 * to create an instance of it.
 */
public interface Instantiator {
    /**
     * Creates an instance of a certain class, or null if the instantiator
     * can't handle it.
     *
     * @param clazz class to instantiate
     * @return an instance of listenerClass, or null to skip this instantiator
     * @throws ReflectiveOperationException if the instantiator can handle this type of class,
     *                                      but an error occured during instantiation
     */
    <T> T newInstance(Class<T> clazz) throws ReflectiveOperationException;
}
