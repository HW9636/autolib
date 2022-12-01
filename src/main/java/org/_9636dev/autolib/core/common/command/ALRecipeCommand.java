package org._9636dev.autolib.core.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ALRecipeCommand {


    public static LiteralArgumentBuilder<CommandSourceStack> getBuilder() {
        return Commands.literal("alrecipe")
                .requires((source) -> source.hasPermission(1))
                .then(Commands.literal("crafting")
                        .then(Commands.argument("result", ResourceLocationArgument.id())
                                .suggests(new CraftingRecipeSuggestionProvider())
                                .executes(craftingCommand))
                );
    }

    private static final Command<CommandSourceStack> craftingCommand = (context) -> {
        ResourceLocation itemLocation = ResourceLocationArgument.getId(context, "result");
        Item item = ForgeRegistries.ITEMS.getValue(itemLocation);

        if (item == Items.AIR) {
            context.getSource().sendFailure(new TextComponent("Invalid Item: '" + itemLocation + "'"));
            return 0;
        }

        List<CraftingRecipe> recipes = context.getSource().getLevel().getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING).stream()
                .filter(r -> r.getResultItem().is(item)).toList();

        context.getSource().sendSuccess(new TextComponent(stringifyCraftingRecipes(recipes)), true);

        return 1;
    };

    private static String stringifyCraftingRecipes(List<CraftingRecipe> craftingRecipes) {
        StringBuilder sb = new StringBuilder().append('[');

        for (CraftingRecipe craftingRecipe : craftingRecipes) {
            try {
                if (craftingRecipe.getClass().getMethod("toString").getDeclaringClass() != Object.class) {
                    sb.append(craftingRecipe).append(',');
                    continue;
                }
            } catch (NoSuchMethodException ignored) {}

            sb.append(craftingRecipe.getClass().getSimpleName()).append("{ingredients:[");
            for (Ingredient ingredient : craftingRecipe.getIngredients()) {
                sb.append(ingredient.toJson()).append(',');
            }
            int c = sb.lastIndexOf(",");
            if (c >= 0) sb.deleteCharAt(c);
            sb.append("], result:").append(craftingRecipe.getResultItem());
            sb.append("},");
        }

        int comma = sb.lastIndexOf(",");
        if (comma >= 0) sb.deleteCharAt(comma);
        return sb.append(']').toString();
    }
}
