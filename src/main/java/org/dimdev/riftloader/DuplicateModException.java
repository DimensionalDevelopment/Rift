package org.dimdev.riftloader;

public class DuplicateModException extends RuntimeException {
	private static final long serialVersionUID = 8227628664058325482L;

    private final ModInfo mod1;
    private final ModInfo mod2;

    public DuplicateModException(ModInfo mod1, ModInfo mod2) {
        if (!mod1.id.equals(mod2.id)) {
            throw new IllegalArgumentException();
        }

        this.mod1 = mod1;
        this.mod2 = mod2;
    }

    @Override
    public String getMessage() {
        return "Duplicate mod " + mod1.id + ":\r\n - " + mod1.source + "\r\n - " + mod2.source;
    }
}
