package com.neuromuser.eatingpace.mixin;

import com.neuromuser.eatingpace.EatingPace;
import com.neuromuser.eatingpace.config.ConfigManager;
import com.neuromuser.eatingpace.config.CustomFoodComponent;
import com.neuromuser.eatingpace.config.ModifiedFoods;
import net.minecraft.item.FoodComponent;
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

        if (stack.getItem().getFoodComponent() == null) {
            return;
        }

        int originalTime = getOriginalVanillaTime(stack);

        if (ConfigManager.get().enableCustomFoodValues) {
            CustomFoodComponent customFood = ModifiedFoods.getCustomFood(stack.getItem());
            if (customFood != null) {
                originalTime = customFood.getEatTicks();
            }
        }

        float multiplier = ConfigManager.get().eatingSpeedMultiplier;
        int finalTime = Math.max(1, (int)(originalTime * multiplier));

        EatingPace.LOGGER.debug("Item: {}, Original: {}, Multiplier: {}, Final: {}",
                stack.getItem().getName().getString(), originalTime, multiplier, finalTime);

        cir.setReturnValue(finalTime);
    }

    private int getOriginalVanillaTime(ItemStack stack) {
        FoodComponent food = stack.getItem().getFoodComponent();
        if (food == null) return 32;

        int defaultTime = 32;

        try {
            String itemName = stack.getItem().getTranslationKey().toLowerCase();
            if (itemName.contains("berry") || itemName.contains("snack") ||
                    stack.getItem().isFood() && food.isSnack()) {
                defaultTime = 16;
            }
        } catch (Exception e) {
        }

        return defaultTime;
    }
}