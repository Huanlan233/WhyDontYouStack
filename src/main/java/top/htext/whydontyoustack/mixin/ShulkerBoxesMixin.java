package top.htext.whydontyoustack.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

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