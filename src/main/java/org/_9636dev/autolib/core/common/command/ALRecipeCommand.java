package org._9636dev.autolib.core.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ALRecipeCommand {


    public static LiteralArgumentBuilder<CommandSourceStack> getBuilder() {
        return Commands.literal("alrecipe")
                .requires((source) -> source.hasPermission(1))
                .then(Commands.literal("crafting")
                        .then(Commands.argument("result", StringArgumentType.string())
                                .suggests(new CraftingRecipeSuggestionProvider())
                                .executes(craftingCommand))
                );
    }

    private static final Command<CommandSourceStack> craftingCommand = (context) -> {
        String itemLocation = context.getArgument("result", String.class).trim();
        if (!itemLocation.contains(":")) itemLocation = "minecraft:" + itemLocation;
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemLocation));

        if (item == null) {
            context.getSource().sendFailure(new TextComponent("Invalid Item: '" + itemLocation + "'"));
            return 0;
        }
        List<CraftingRecipe> recipes =  context.getSource().getLevel().getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING).stream()
                .filter(r -> r.getResultItem().is(item)).toList();

        LogUtils.getLogger().info("Recipes: {}", recipes);

        return 1;
    };
}
