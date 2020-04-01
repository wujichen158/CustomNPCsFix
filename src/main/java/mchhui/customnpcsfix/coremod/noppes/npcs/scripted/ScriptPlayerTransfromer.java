package mchhui.customnpcsfix.coremod.noppes.npcs.scripted;

import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.launchwrapper.IClassTransformer;

public class ScriptPlayerTransfromer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals("noppes.npcs.scripted.ScriptPlayer")) {
            FMLLog.getLogger().warn("[Transforming:noppes.npcs.scripted.ScriptPlayer]");
            ClassNode classNode = new ClassNode(Opcodes.ASM5);
            ClassReader classReader = new ClassReader(basicClass);
            classReader.accept(classNode, 0);
            List<MethodNode> methods = classNode.methods;
            for (MethodNode method : methods) {
                if (method.name.equals("sendMessage")) {
                    InsnList list = new InsnList();
                    list.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "mchhui/customnpcsfix/util/NPCHelper", "onSay",
                            "(Ljava/lang/String;)Ljava/lang/String;", false));
                    list.add(new VarInsnNode(Opcodes.ASTORE, 1));
                    list.add(new LabelNode());
                    method.instructions.insert(method.instructions.getFirst(), list);
                }
            }
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(classWriter);
            FMLLog.getLogger().warn("[Transformed:noppes.npcs.scripted.ScriptPlayer]");
            return classWriter.toByteArray();
        }
        return basicClass;
    }

}
