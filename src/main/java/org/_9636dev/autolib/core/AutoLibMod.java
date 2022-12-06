package org._9636dev.autolib.core;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(AutoLibMod.MODID)
public class AutoLibMod
{
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "autolib";

    public AutoLibMod()
    {
        LOGGER.debug("Registering {}", MODID);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)  {

    }

    private void clientSetup(final FMLClientSetupEvent event) {

    }


    private void processIMC(final InterModProcessEvent event)
    {
        event.getIMCStream().forEach((message) -> {

        });
    }

}
