package org.dimdev.accesstransform;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class AccessTransformer {
    private final AccessTransformationSet transformations;

    public AccessTransformer(AccessTransformationSet transformations) {
        this.transformations = transformations;
    }

    public byte[] transformClass(String name, byte[] bytes) {
        if (bytes == null || !transformations.isClassAffected(name)) {
            return bytes;
        }

        ClassNode clazz = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(clazz, 0);

        // Transform class access level
        clazz.access = getNewAccessLevel(new ElementReference(ElementReference.Kind.CLASS, null, name, null), clazz.access);

        for (FieldNode field : clazz.fields) {
            field.access = getNewAccessLevel(new ElementReference(ElementReference.Kind.FIELD, name, field.name, field.desc), field.access);
        }

        for (MethodNode method : clazz.methods) {
            method.access = getNewAccessLevel(new ElementReference(ElementReference.Kind.METHOD, name, method.name, method.desc), method.access);
        }

        ClassWriter writer = new ClassWriter(0);
        clazz.accept(writer);
        return writer.toByteArray();
    }

    private int getNewAccessLevel(ElementReference elementReference, int access) {
        AccessLevel minimumAccessLevel = transformations.getMinimumAccessLevel(elementReference);
        if (minimumAccessLevel == null) {
            return access;
        }

        AccessLevel.Visibility visibility;
        if ((access & Opcodes.ACC_PUBLIC) != 0)  {
            visibility = AccessLevel.Visibility.PUBLIC;
            access &= ~Opcodes.ACC_PUBLIC;
        } else if ((access & Opcodes.ACC_PROTECTED) != 0)  {
            visibility = AccessLevel.Visibility.PROTECTED;
            access &= ~Opcodes.ACC_PROTECTED;
        } else if ((access & Opcodes.ACC_PRIVATE) != 0)  {
            visibility = AccessLevel.Visibility.PRIVATE;
            access &= ~Opcodes.ACC_PRIVATE;
        } else {
            visibility = AccessLevel.Visibility.DEFAULT;
        }
        boolean isFinal = (access & Opcodes.ACC_FINAL) != 0;

        AccessLevel newAccessLevel = AccessLevel.union(minimumAccessLevel, new AccessLevel(visibility, isFinal));
        if (newAccessLevel == null) {
            return access;
        }

        if (isFinal && !newAccessLevel.isFinal) {
            access &= ~Opcodes.ACC_FINAL;
        }

        switch (newAccessLevel.visibility) {
            case PUBLIC: {
                return access | Opcodes.ACC_PUBLIC;
            }

            case PROTECTED: {
                return access | Opcodes.ACC_PROTECTED;
            }

            case DEFAULT: {
                return access;
            }

            case PRIVATE: {
                return access | Opcodes.ACC_PRIVATE;
            }

            default: {
                throw new RuntimeException("Unknown visibility level '" + newAccessLevel.visibility + "'");
            }
        }
    }
}
