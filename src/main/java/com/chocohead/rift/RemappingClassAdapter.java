package com.chocohead.rift;

import java.util.Map;
import java.util.Set;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.MethodRemapper;
import org.objectweb.asm.commons.Remapper;

import com.google.common.collect.ImmutableSet;

public class RemappingClassAdapter extends ClassRemapper {
	private static class BlobRemapper extends Remapper {
		private final Map<String, ClassMapping> mappings;

		public BlobRemapper(MappingBlob mapping) {
			mappings = mapping.mappings;
		}

		@Override
		public String map(String name) {
			ClassMapping map = mappings.get(name);
			return map != null ? map.mcpName : name;
		}

		@Override
		public String mapFieldName(String owner, String name, String descriptor) {
			ClassMapping map = mappings.get(owner);
			if (map == null) return name;

			return map.fields.getOrDefault(name + ";;" + descriptor, name);
		}

		@Override
		public String mapMethodName(String owner, String name, String descriptor) {
			ClassMapping map = mappings.get(owner);
			if (map == null) return name;

			return map.methods.getOrDefault(name + descriptor, name);
		}
	}

	public RemappingClassAdapter(ClassVisitor cv, MappingBlob mapping) {
		super(cv, new BlobRemapper(mapping));
	}

	@Override
	protected MethodVisitor createMethodRemapper(MethodVisitor mv) {
		return new AsmMethodRemapper(mv, remapper);
	}

	private static class AsmMethodRemapper extends MethodRemapper {
		private static final Set<Handle> META_FACTORIES = ImmutableSet.of(
                new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/LambdaMetafactory", "metafactory",
                        "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;"
                                + "Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;",
                        false),
                new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/LambdaMetafactory", "altMetafactory",
                        "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;[Ljava/lang/Object;)"
                                + "Ljava/lang/invoke/CallSite;",
                        false)
        );

		public AsmMethodRemapper(MethodVisitor mv, Remapper remapper) {
			super(mv, remapper);
		}

		@Override
        public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
            if (META_FACTORIES.contains(bsm)) {
                String owner = Type.getReturnType(desc).getInternalName();
                String ownerDesc = ((Type) bsmArgs[0]).getDescriptor();

                name = remapper.mapMethodName(owner, name, ownerDesc);
            }

            super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
        }
	}
}