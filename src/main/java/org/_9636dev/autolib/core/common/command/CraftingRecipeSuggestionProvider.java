package org._9636dev.autolib.core.common.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CraftingRecipeSuggestionProvider implements SuggestionProvider<CommandSourceStack> {

    public CraftingRecipeSuggestionProvider() {

    }

    public Set<String> getAllResultItemIds(Level level) {
        return level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING).stream().map(Recipe::getResultItem).map(ItemStack::toString).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        LogUtils.getLogger().info("Argument result: {}", context.getArgument("result", String.class));
        getAllResultItemIds(context.getSource().getLevel()).forEach(builder::suggest);
        return CompletableFuture.completedFuture(builder.build());
    }
}
