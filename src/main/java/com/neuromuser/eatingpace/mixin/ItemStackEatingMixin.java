package com.neuromuser.eatingpace.mixin;

import com.neuromuser.eatingpace.EatingPace;
import com.neuromuser.eatingpace.config.ConfigManager;
import com.neuromuser.eatingpace.config.CustomFoodComponent;
import com.neuromuser.eatingpace.config.ModifiedFoods;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackEatingMixin {
    @Inject(method = "getMaxUseTime", at = @At("HEAD"), cancellable = true)
    private void modifyMaxUseTime(LivingEntity user, CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack)(Object)this;

        FoodComponent food = stack.get(DataComponentTypes.FOOD);
        if (food == null) {
            return;
        }

        int originalTicks = getOriginalVanillaTicks(food);

        if (ConfigManager.get().enableCustomFoodValues) {
            CustomFoodComponent customFood = ModifiedFoods.getCustomFood(stack.getItem());
            if (customFood != null) {
                originalTicks = customFood.getEatTicks();
            }
        }

        float multiplier = ConfigManager.get().eatingSpeedMultiplier;
        int finalTime = Math.max(1, (int)(originalTicks * multiplier));

        cir.setReturnValue(finalTime);
    }

    @Unique
    private int getOriginalVanillaTicks(FoodComponent food) {
        return food.getEatTicks();
    }
}