package com.neuromuser.eatingpace.mixin;

import com.neuromuser.eatingpace.config.CustomFoodComponent;
import com.neuromuser.eatingpace.config.ModifiedFoods;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackEatingMixin {
    @Inject(method = "getMaxUseTime", at = @At("HEAD"), cancellable = true)
    private void modifyMaxUseTime(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack)(Object)this;

        if (stack.getItem().getFoodComponent() != null) {
            CustomFoodComponent customFood = ModifiedFoods.getCustomFood(stack.getItem());

            if (customFood != null) {
                cir.setReturnValue(customFood.getEatTicks());
            }
        }
    }
}