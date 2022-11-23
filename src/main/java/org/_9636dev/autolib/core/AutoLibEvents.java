package org._9636dev.autolib.core;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org._9636dev.autolib.core.common.command.ALRecipeCommand;

@Mod.EventBusSubscriber(modid = AutoLibMod.MODID)
public class AutoLibEvents {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(ALRecipeCommand.getBuilder());
    }
}
