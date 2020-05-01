package mchhui.customnpcsfix.listener.client;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

import mchhui.customnpcsfix.Config;
import mchhui.customnpcsfix.api.event.client.ClientSendDataEvent;
import mchhui.customnpcsfix.client.gui.HueihueaGuiQuestEdit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import noppes.npcs.client.gui.global.GuiNPCManageQuest;
import noppes.npcs.client.gui.global.GuiQuestEdit;
import noppes.npcs.client.gui.script.GuiScriptInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.constants.EnumPacketServer;
import xaero.common.api.spigot.ServerWaypoint;
import xaero.common.gui.GuiWaypoints;
import xaero.common.minimap.waypoints.Waypoint;

public class ClientListener {
    @SubscribeEvent
    public void onClientSendData(ClientSendDataEvent event) {
        if (!Config.DontSendDubiousScript) {
            return;
        }
        EnumPacketServer type = event.type;
        if (type != EnumPacketServer.ScriptBlockDataSave && type != EnumPacketServer.ScriptDataSave
                && type != EnumPacketServer.ScriptDoorDataSave && type != EnumPacketServer.ScriptForgeSave
                && type != EnumPacketServer.ScriptItemDataSave && type != EnumPacketServer.ScriptPlayerSave) {
            return;
        }
        NBTTagCompound nbt = null;
        if (type == EnumPacketServer.ScriptBlockDataSave) {
            nbt = (NBTTagCompound) event.obs[3];
        } else if (type == EnumPacketServer.ScriptDoorDataSave) {
            nbt = (NBTTagCompound) event.obs[3];
        } else {
            nbt = (NBTTagCompound) event.obs[0];
        }
        if (nbt.getTagList("Scripts", 10).hasNoTags()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent event) {
        if (event.phase != Phase.END) {
            return;
        }
        GuiScreen gui = Minecraft.getMinecraft().currentScreen;
        if (gui instanceof GuiWaypoints) {
            List<Waypoint> list;
            ConcurrentSkipListSet<Integer> selectedListSet;
            Class GuiWaypointsClass = GuiWaypoints.class;
            try {
                Field selectedListSetField = GuiWaypointsClass.getDeclaredField("selectedListSet");
                selectedListSetField.setAccessible(true);
                selectedListSet = (ConcurrentSkipListSet<Integer>) selectedListSetField.get(gui);
                Method getSelectedWaypointsListMethod = GuiWaypointsClass.getDeclaredMethod("getSelectedWaypointsList",
                        boolean.class);
                getSelectedWaypointsListMethod.setAccessible(true);
                list = (List<Waypoint>) getSelectedWaypointsListMethod.invoke(gui, true);
                int i = 0;
                List<Integer> removeList = new ArrayList<Integer>();
                for (Waypoint point : list) {
                    if (point.getType() == 1001) {
                        removeList.add(i);
                    }
                    i++;
                }
                i = 0;
                for (Integer in : selectedListSet) {
                    if (removeList.contains(i)) {
                        selectedListSet.remove(in);
                    }
                    i++;
                }
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchFieldException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        }
        if (gui instanceof GuiScriptInterface) {
            GuiScriptInterface scriptgui = (GuiScriptInterface) gui;
            if (scriptgui.handler.getScripts().size() >= 11 && Config.LimitedScriptGuiAddButton) {
                scriptgui.getTopButton(12).enabled = false;
                scriptgui.getTopButton(12).visible = false;
            }
        }
        if (gui instanceof GuiNPCManageQuest) {
            GuiNPCManageQuest manageQuestGui = (GuiNPCManageQuest) gui;
            if (manageQuestGui.getSubGui() instanceof GuiQuestEdit) {
                GuiQuestEdit questgui = (GuiQuestEdit) manageQuestGui.getSubGui();
                if (!(questgui instanceof HueihueaGuiQuestEdit)) {
                    questgui = HueihueaGuiQuestEdit.create(questgui);
                    manageQuestGui.setSubGui(questgui);
                }
            }
        }
    }
}
