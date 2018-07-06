package org.dimdev.riftloader.listener;

/**
 * This listener is called during the Tweaker initialization phase, before Minecraft
 * has started. It allows you to do things like adding tweakers and mixins.
 */
public interface InitializationListener {
    void onInitialization();
}
