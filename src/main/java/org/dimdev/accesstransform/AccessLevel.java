package org.dimdev.accesstransform;

import java.util.Locale;

public class AccessLevel {
    public enum Visibility {
        PRIVATE,
        DEFAULT,
        PROTECTED,
        PUBLIC
    }

    public final Visibility visibility;
    public final boolean isFinal;

    public AccessLevel(Visibility visibility, boolean isFinal) {
        this.visibility = visibility;
        this.isFinal = isFinal;
    }

    public static AccessLevel union(AccessLevel first, AccessLevel second) {
        if (first == null) {
            return second;
        } else if (second == null) {
            return first;
        }

        Visibility visibility = first.visibility.ordinal() < second.visibility.ordinal() ? second.visibility : first.visibility;
        boolean isFinal = first.isFinal && second.isFinal;

        return new AccessLevel(visibility, isFinal);
    }

    public static AccessLevel fromString(String string) {
        string = string.toUpperCase(Locale.ROOT);
        boolean isFinal = true;
        if (string.endsWith("-F")) {
            isFinal = false;
            string = string.substring(0, string.length() - 2);
        }

        return new AccessLevel(Visibility.valueOf(string), isFinal);
    }
}
