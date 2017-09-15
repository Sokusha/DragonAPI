package Reika.DragonAPI.ASM.Patchers.Hooks.Event.Render;

import net.minecraftforge.classloading.FMLForgePlugin;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import Reika.DragonAPI.ASM.Patchers.Patcher;
import Reika.DragonAPI.Libraries.Java.ReikaASMHelper;


public class EntityRenderLoop extends Patcher {

	public EntityRenderLoop() {
		super("net.minecraft.client.renderer.EntityRenderer", "blt");
	}

	@Override
	protected void apply(ClassNode cn) {
		MethodNode m = ReikaASMHelper.getMethodByName(cn, "func_78471_a", "renderWorld", "(FJ)V");

		InsnList li = new InsnList();
		li.add(new VarInsnNode(Opcodes.FLOAD, 1));
		li.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "Reika/DragonAPI/Instantiable/Event/Client/EntityRenderingLoopEvent", "fire", "(F)V", false));

		String func = FMLForgePlugin.RUNTIME_DEOBF ? "func_147589_a" : "renderEntities";
		String sig = "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/client/renderer/culling/ICamera;F)V";
		MethodInsnNode min = ReikaASMHelper.getFirstMethodCall(cn, m, "net/minecraft/client/renderer/RenderGlobal", func, sig);

		m.instructions.insert(min, ReikaASMHelper.copyInsnList(li));

		min = ReikaASMHelper.getNthMethodCall(cn, m, "net/minecraft/client/renderer/RenderGlobal", func, sig, 2);
		m.instructions.insert(min, ReikaASMHelper.copyInsnList(li));
	}

}
