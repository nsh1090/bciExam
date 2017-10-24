import org.objectweb.asm.*;

import java.io.FileOutputStream;

import static org.objectweb.asm.Opcodes.*;

public class ChangeTargetAsm extends ClassLoader {

    public static void main(String[] args) throws Exception {

        final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassReader cr = new ClassReader("TargetClass");

        cr.accept(new ClassVisitor(ASM4, cw){

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

                MethodVisitor mv = cv.visitMethod(access, name, desc, signature,
                        exceptions);
                return new MethodVisitor(Opcodes.ASM5, mv) {

                    @Override
                    public void visitLdcInsn(Object cst) {


                        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
                        mv.visitInsn(DUP);
                        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
                        mv.visitLdcInsn(name + " ");
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

                        super.visitLdcInsn(cst);
                    }
                };

            }

        },0);

        byte[] code = cw.toByteArray();

        FileOutputStream fos = new FileOutputStream("TargetClass.class");
        fos.write(code);
        fos.close();


        ChangeTargetAsm loader = new ChangeTargetAsm();
        Class<?> exampleClass = loader.defineClass("TargetClass", code, 0, code.length);

        exampleClass.getMethods()[0].invoke(null, new Object[] { null });
    }

}
