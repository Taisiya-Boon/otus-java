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
import java.util.Objects;

import static java.util.Map.entry;
import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;

public class Agent {

    private static final ArrayList<MethodInfo> methods = new ArrayList<>();
    private static final HashMap<MethodInfo.Descriptor, String> descCache = new HashMap<>();

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
                for (MethodInfo method : methods) {
                    if (method.getName().equals(name) && method.getDescriptor().getDesc().equals(descriptor) && !method.getAnnotationList().isEmpty()) {
                        return super.visitMethod(access, name + "Proxied", descriptor, signature, exceptions);
                    }
                }
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }
        };
        cr.accept(cv, Opcodes.ASM5);

        for (MethodInfo method : methods) {
            if (!method.getAnnotationList().isEmpty()) {
                MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, method.getName(), method.getDescriptor().getDesc(), null, null);

                Handle handle = new Handle(
                        H_INVOKESTATIC,
                        Type.getInternalName(java.lang.invoke.StringConcatFactory.class),
                        "makeConcatWithConstants",
                        MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class, Object[].class).toMethodDescriptorString(),
                        false);

                mv.visitCode();

                if (method.getAnnotationList().contains("Lru/otus/Log;")) {
                    setOnType(method.getDescriptor());
                    for (int i = 1; i <= method.getDescriptor().getParam().size(); i++) {
                        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

                        mv.visitVarInsn(setOnOpcodes(method.getDescriptor().getParam().get(i-1)), i);

                        if (method.getDescriptor().getParam().size() == 1) {
                            mv.visitInvokeDynamicInsn("makeConcatWithConstants", "(" + method.getDescriptor().getParam().get(i-1) + ")Ljava/lang/String;", handle, "executed method: " + method.getName() + ", param: \u0001");

                            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                        } else {
                            if (i == 1) {
                                mv.visitInvokeDynamicInsn("makeConcatWithConstants", "(" + method.getDescriptor().getParam().get(i - 1) + ")Ljava/lang/String;", handle, "executed method: " + method.getName() + ", param " + i + ": \u0001");

                                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);
                            } else if (i == method.getDescriptor().getParam().size()) {
                                mv.visitInvokeDynamicInsn("makeConcatWithConstants", "(" + method.getDescriptor().getParam().get(i - 1) + ")Ljava/lang/String;", handle, ", param " + i + ": \u0001");

                                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                            } else {
                                mv.visitInvokeDynamicInsn("makeConcatWithConstants", "(" + method.getDescriptor().getParam().get(i - 1) + ")Ljava/lang/String;", handle, ", param " + i + ": \u0001");

                                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);
                            }
                        }
                    }
                }

                mv.visitVarInsn(Opcodes.ALOAD, 0);
                setOnType(method.getDescriptor());
                int i = 1;
                for (String desc : method.getDescriptor().getParam()) {
                    mv.visitVarInsn(setOnOpcodes(desc), i);
                    i++;
                }
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "ru/otus/MyClass", method.getName() + "Proxied", method.getDescriptor().getDesc(), false);

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
                methods.add(new MethodInfo(name, descriptor));
                return new MethodAnnotationScanner(super.visitMethod(access, name, descriptor, signature, exceptions));
            }
        };
        reader.accept(visitor, Opcodes.ASM5);
    }

    private static String setOnType(MethodInfo.Descriptor desc) {
        if (descCache.containsKey(desc)) {
            return descCache.get(desc);
        } else {
            boolean flag = false;
            boolean flagDesc = false;
            StringBuilder type = new StringBuilder();
            StringBuilder typeDesc = new StringBuilder();
            for (char ch : desc.getDesc().toCharArray()) {
                if (ch == ')') {
                    flag = false;
                }
                if (flag) {
                    if (ch == 'L') {
                        flagDesc = true;
                    }
                    if (flagDesc) {
                        typeDesc.append(ch);
                    }
                    if (ch == ';') {
                        flagDesc = false;
                    }
                    if (!flagDesc) {
                        if (typeDesc.toString().equals("")) {
                            desc.addParam("" + ch);
                        } else {
                            desc.addParam(typeDesc.toString());
                            typeDesc.delete(0, typeDesc.toString().length() - 1);
                        }
                    }
                    type.append(ch);
                }
                if (ch == '(') {
                    flag = true;
                }
            }
            descCache.put(desc, type.toString());
            return type.toString();
        }
    }

    private static int setOnOpcodes(String desc) {
        return setOnTypeMap.getOrDefault(desc, Opcodes.ALOAD);
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
                methods.get(methods.size()-1).addAnnotation(desc);
            }
            return super.visitAnnotation(desc, visible);
        }

    }

    private static class MethodInfo {

        private final String name;
        private final Descriptor descriptor;
        private final ArrayList<String> annotation = new ArrayList<>();

        public MethodInfo(String name, String descriptor) {
            this.name = name;
            this.descriptor = new Descriptor(descriptor);
        }

        public String getName() {
            return name;
        }

        public Descriptor getDescriptor() {
            return descriptor;
        }

        public ArrayList<String> getAnnotationList() {
            return annotation;
        }

        public void addAnnotation(String annotationDesc) {
            annotation.add(annotationDesc);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MethodInfo)) return false;
            MethodInfo that = (MethodInfo) o;
            return name.equals(that.name) &&
                    descriptor.equals(that.descriptor) &&
                    Objects.equals(annotation, that.annotation);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, descriptor, annotation);
        }

        private static class Descriptor {

            private final String desc;
            private final ArrayList<String> param = new ArrayList<>();

            public Descriptor(String desc) {
                this.desc = desc;
            }

            public ArrayList<String> getParam() {
                return param;
            }

            public String getDesc() {
                return desc;
            }

            public void addParam(String param) {
                this.param.add(param);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Descriptor)) return false;
                Descriptor that = (Descriptor) o;
                return Objects.equals(desc, that.desc) &&
                        param.equals(that.param);
            }

            @Override
            public int hashCode() {
                return Objects.hash(desc, param);
            }
        }
    }

}

