package ru.otus;

import org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;
import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;

public class Agent {

    private static final ArrayList<String> methodsName = new ArrayList<>();
    private static final HashMap<String, String> descMethods = new HashMap<>();
    private static final HashMap<String, String> annotationMethods = new HashMap<>();

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {
                if (className.equals("ru/otus/MyClass")) {
                    return addLogMethod(classfileBuffer);
                }
                return classfileBuffer;
            }
        });

    }

    private static byte[] addLogMethod(byte[] originalClass) {
        searchAnnotationMethods(originalClass);

        ClassReader cr = new ClassReader(originalClass);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM7, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                if (annotationMethods.containsKey(name)) {
                    return super.visitMethod(access, name + "Proxied", descriptor, signature, exceptions);
                } else {
                    return super.visitMethod(access, name, descriptor, signature, exceptions);
                }
            }
        };
        cr.accept(cv, Opcodes.ASM5);


        for (Map.Entry<String, String> name : annotationMethods.entrySet()) {
            if (name.getValue().equals("Lru/otus/Log;")) {
                MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name.getKey(), descMethods.get(name.getKey()), null, null);

                Handle handle = new Handle(
                        H_INVOKESTATIC,
                        Type.getInternalName(java.lang.invoke.StringConcatFactory.class),
                        "makeConcatWithConstants",
                        MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class, Object[].class).toMethodDescriptorString(),
                        false);

                mv.visitCode();

                mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                mv.visitVarInsn(setOnOpcodes(descMethods.get(name.getKey())), 1);
                mv.visitInvokeDynamicInsn("makeConcatWithConstants", "(" + setOnType(descMethods.get(name.getKey())) + ")Ljava/lang/String;", handle, "executed method: " + name.getKey() + ", param: \u0001");

                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitVarInsn(setOnOpcodes(descMethods.get(name.getKey())), 1);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "ru/otus/MyClass", name.getKey() + "Proxied", descMethods.get(name.getKey()), false);

                mv.visitInsn(Opcodes.RETURN);
                mv.visitMaxs(0, 0);
                mv.visitEnd();
            }
        }

        byte[] finalClass = cw.toByteArray();

        try (OutputStream fos = new FileOutputStream("proxyASM.class")) {
            fos.write(finalClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalClass;
    }

    private static void searchAnnotationMethods(byte[] originalClass) {
        ClassReader reader = new ClassReader(originalClass);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
        ClassVisitor visitor = new ClassVisitor(Opcodes.ASM7, writer) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                methodsName.add(name);
                descMethods.put(name, descriptor);
                return new MethodAnnotationScanner(super.visitMethod(access, name, descriptor, signature, exceptions));
            }
        };
        reader.accept(visitor, Opcodes.ASM5);
    }

    private static String setOnType(String desc) {
        boolean flag = false;
        StringBuilder type = new StringBuilder();
        for (char ch : desc.toCharArray()) {
            if (ch ==')') {
                flag = false;
            }
            if (flag) {
                type.append(ch);
            }
            if (ch == '(') {
                flag = true;
            }
        }
        return type.toString();
    }

    private static int setOnOpcodes(String desc) {
        String type = setOnType(desc);
        return setOnTypeMap.getOrDefault(type, Opcodes.ALOAD);
    }

    private static final Map<String, Integer> setOnTypeMap = Map.ofEntries(
            entry("I", Opcodes.ILOAD),
            entry("C", Opcodes.ALOAD),
            entry("B", Opcodes.ALOAD),
            entry("S", Opcodes.ALOAD),
            entry("Z", Opcodes.ALOAD),
            entry("F", Opcodes.FLOAD),
            entry("J", Opcodes.LLOAD),
            entry("D", Opcodes.DLOAD),
            entry("Ljava/lang/String;", Opcodes.ALOAD)
    );


    private static class MethodAnnotationScanner extends MethodVisitor {

        public MethodAnnotationScanner(MethodVisitor methodVisitor) {
            super(Opcodes.ASM5, methodVisitor);
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            if (visible && desc.equals("Lru/otus/Log;")) {
                annotationMethods.put(methodsName.get(methodsName.size() - 1), desc);
            }
            return super.visitAnnotation(desc, visible);
        }

    }

}

