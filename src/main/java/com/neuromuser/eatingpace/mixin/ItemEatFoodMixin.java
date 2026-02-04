package com.neuromuser.eatingpace.mixin;

import com.neuromuser.eatingpace.config.ConfigManager;
import com.neuromuser.eatingpace.config.CustomFoodComponent;
import com.neuromuser.eatingpace.config.ModifiedFoods;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Item.class)
public class ItemEatFoodMixin {

    @ModifyArg(
            method = "finishUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;eatFood(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/component/type/FoodComponent;)Lnet/minecraft/item/ItemStack;"
            ),
            index = 2
    )
    private FoodComponent modifyFoodComponent(FoodComponent original) {
        if (!ConfigManager.get().enableCustomFoodValues) {
            return original;
        }

        Item thisItem = (Item) (Object) this;
        CustomFoodComponent customFood = ModifiedFoods.getCustomFood(thisItem);

        if (customFood != null) {
            return customFood.toVanillaComponent();
        }

        return original;
    }
}