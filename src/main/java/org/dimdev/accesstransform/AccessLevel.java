package org.dimdev.accesstransform;

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
        boolean isFinal = true;
        if (string.endsWith("-f")) {
            isFinal = false;
            string = string.substring(0, string.length() - 2);
        }

        switch (string) {
            case "private": {
                return new AccessLevel(Visibility.PRIVATE, isFinal);
            }

            case "default": {
                return new AccessLevel(Visibility.DEFAULT, isFinal);
            }

            case "protected": {
                return new AccessLevel(Visibility.PROTECTED, isFinal);
            }

            case "public": {
                return new AccessLevel(Visibility.PUBLIC, isFinal);
            }

            default: {
                throw new RuntimeException("Unknown visibility '" + string + "'");
            }
        }
    }
}
