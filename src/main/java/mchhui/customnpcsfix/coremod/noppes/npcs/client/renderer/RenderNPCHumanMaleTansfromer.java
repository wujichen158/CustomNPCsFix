package mchhui.customnpcsfix.coremod.noppes.npcs.client.renderer;

import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.launchwrapper.IClassTransformer;

public class RenderNPCHumanMaleTansfromer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals("noppes.npcs.client.renderer.RenderNPCHumanMale")) {
            FMLLog.getLogger().warn("[Transforming:noppes.npcs.client.renderer.RenderNPCHumanMale]");
            ClassNode classNode = new ClassNode(Opcodes.ASM5);
            ClassReader classReader = new ClassReader(basicClass);
            classReader.accept(classNode, 0);
            List<MethodNode> methods = classNode.methods;
            for (MethodNode method : methods) {
                if (method.desc.equals("(Lnoppes/npcs/entity/EntityNPCInterface;DDDFF)V")) {
                    for (AbstractInsnNode node : method.instructions.toArray()) {
                        if (node.getOpcode() != Opcodes.RETURN) {
                            continue;
                        }
                        InsnList list = new InsnList();
                        list.add(new LabelNode());
                        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        list.add(new VarInsnNode(Opcodes.ALOAD, 1));
                        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "mchhui/customnpcsfix/api/EventHook",
                                "onPostRenderNPCHumanMale",
                                "(Lnoppes/npcs/client/renderer/RenderNPCHumanMale;Lnoppes/npcs/entity/EntityNPCInterface;)V",
                                false));
                        method.instructions.insertBefore(node.getPrevious(), list);
                    }
                    InsnList list = new InsnList();
                    LabelNode label = new LabelNode();
                    list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    list.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "mchhui/customnpcsfix/api/EventHook",
                            "onPreRenderNPCHumanMale",
                            "(Lnoppes/npcs/client/renderer/RenderNPCHumanMale;Lnoppes/npcs/entity/EntityNPCInterface;)Z",
                            false));
                    list.add(new JumpInsnNode(Opcodes.IFEQ, label));
                    list.add(new InsnNode(Opcodes.RETURN));
                    list.add(label);
                    method.instructions.insert(method.instructions.getFirst(), list);
                }
            }
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            classNode.accept(classWriter);
            FMLLog.getLogger().warn("[Transformed:noppes.npcs.client.renderer.RenderNPCHumanMale]");
            return classWriter.toByteArray();
        }
        return basicClass;
    }
}
