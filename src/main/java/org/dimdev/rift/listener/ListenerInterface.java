package org.dimdev.rift.listener;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An informative annotation type used to indicate that an interface
 * type declaration is intended to be a <i>listener interface</i>.
 * However, Rift will treat any given interface as a listener interface
 * at runtime, regardless of whether or not a {@code ListenerInterface}
 * annotation is present on the interface declaration.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ListenerInterface {}
