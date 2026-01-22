package com.neuromuser.eatingpace.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.neuromuser.eatingpace.EatingPace;
import com.neuromuser.eatingpace.config.CustomFoodComponent;
import com.neuromuser.eatingpace.config.ModifiedFoods;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Item.class)
public abstract class FoodComponentMixin {
    @ModifyReturnValue(method = "getFoodComponent", at = @At("RETURN"))
    private FoodComponent modifyFoodComponent(FoodComponent original) {
        if (!EatingPace.isServerSideActive()){
            return original;
        }

        Item thisItem = (Item)(Object)this;
        CustomFoodComponent customFood = ModifiedFoods.getCustomFood(thisItem);

        if (customFood != null) {
            return customFood.toVanillaComponent();
        }

        return original;
    }
}