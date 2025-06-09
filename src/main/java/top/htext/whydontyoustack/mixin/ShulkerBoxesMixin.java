package top.htext.whydontyoustack.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.logging.Log;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import top.htext.whydontyoustack.WhyDontYouStack;

import javax.swing.*;
import java.util.List;

import static top.htext.whydontyoustack.WhyDontYouStack.*;

@Mixin(ShulkerBoxBlock.class)
public class ShulkerBoxesMixin {
	/**
	 * 将 BlockEntityTag.Items 为空的潜影盒在掉落的时候直接删去此NBT
	 **/
	@Inject(
			at = @At(
					value = "TAIL"
			),
			method = "getDroppedStacks",
			cancellable = true
	)
	void onDroppedStack(BlockState state, LootContextParameterSet.Builder builder, CallbackInfoReturnable<List<ItemStack>> cir) {
		var itemStack = cir.getReturnValue().get(0);
		if (itemStack.getNbt() == null) return;

		var items = itemStack.getNbt().getCompound("BlockEntityTag").getList("Items",10);
		if (!items.stream().toList().isEmpty()) return;

		itemStack.getNbt().remove("BlockEntityTag");
		cir.setReturnValue(List.of(itemStack));
	}
}