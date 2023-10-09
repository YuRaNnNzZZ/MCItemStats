package ru.yurannnzzz.mcitemstats.asm;

import cpw.mods.fml.relauncher.IClassTransformer;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.List;

public class ItemStackTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, byte[] bytes) {
        if (bytes == null || !(name.equals("net.minecraft.item.ItemStack") || name.equals("ur"))) {
            return bytes;
        }

        boolean isObf = !name.equals("net.minecraft.item.ItemStack");
        String methodName = isObf ? "a" : "getTooltip";
        String methodDesc = isObf ? "(Lqx;Z)Ljava/util/List;" : "Lnet/minecraft/item/ItemStack;getTooltip(Lnet/minecraft/entity/player/EntityPlayer;Z)Ljava/util/List;";

        ClassReader cr = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        cr.accept(classNode, 0);

        Label label = new Label();
        LabelNode labelNode = new LabelNode(label);

        InsnList newInsnList = new InsnList();

        newInsnList.add(labelNode);
        newInsnList.add(new VarInsnNode(Opcodes.ALOAD, 0)); // ItemStack
        newInsnList.add(new VarInsnNode(Opcodes.ALOAD, 1)); // EntityPlayer
        newInsnList.add(new VarInsnNode(Opcodes.ILOAD, 2)); // boolean
        newInsnList.add(new VarInsnNode(Opcodes.ALOAD, 3)); // ArrayList
        newInsnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/yurannnzzz/mcitemstats/events/ItemTooltipEventHandler", "handleTooltip", isObf ? "(Lur;Lqx;ZLjava/util/ArrayList;)V" : "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;ZLjava/util/ArrayList;)V"));

        for (MethodNode methodNode : (List<MethodNode>)classNode.methods) {
            if (methodNode.name.equals(methodName) && methodNode.desc.equals(methodDesc)) {
                System.out.println("Found method");

                for (int i = 0; i < methodNode.instructions.size(); i++) {
                    AbstractInsnNode node = methodNode.instructions.get(i);

                    if (node instanceof LineNumberNode && ((LineNumberNode) node).line == 427) {
                        methodNode.instructions.set(node, new JumpInsnNode(Opcodes.GOTO, labelNode));
                    }

                    if (node.getOpcode() == Opcodes.ARETURN) {
                        methodNode.instructions.insertBefore(node.getPrevious(), newInsnList);

                        break;
                    }
                }

                break;
            }
        }

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(cw);

        return cw.toByteArray();
    }
}
