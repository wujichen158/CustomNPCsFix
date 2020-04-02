package mchhui.customnpcsfix;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import mchhui.customnpcsfix.proxy.CommonProxy;

@Mod(modid = CustomNPCsFix.MODID, name = CustomNPCsFix.NAME, version = CustomNPCsFix.VERSION, useMetadata = true, acceptableRemoteVersions = "*")
public class CustomNPCsFix {
    public static final String MODID = "customnpcsfix";
    public static final String NAME = "CustomNPCs Fix";
    public static final String VERSION = "1.2";
    @SidedProxy(modId = CustomNPCsFix.MODID, clientSide = "mchhui.customnpcsfix.proxy.ClientProxy", serverSide = "mchhui.customnpcsfix.proxy.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        proxy.onPreInit(event);
    }

    @EventHandler
    public void onInit(FMLInitializationEvent event) {
        proxy.onInit(event);
    }
    
    @EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        proxy.onServerStart(event);
    }
}