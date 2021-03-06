package ninja.genuine.tooltips;

import ninja.genuine.tooltips.client.config.Config;
import ninja.genuine.tooltips.client.gui.GuiConfigTooltips;
import ninja.genuine.tooltips.client.render.TooltipEvent;
import ninja.genuine.utils.ModUtils;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLModDisabledEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

@Mod(modid = Constants.MODID, name = Constants.NAME, version = Constants.VERSION, canBeDeactivated = true, clientSideOnly = true, updateJSON = Constants.URL
		+ "update.json", useMetadata = true, guiFactory = "ninja.genuine.tooltips.client.TooltipsGuiFactory")
public class WorldTooltips {

	@Instance(Constants.MODID)
	public static WorldTooltips instance;
	private TooltipEvent events = new TooltipEvent();
	private KeyBinding configKey = new KeyBinding(Constants.NAME + " Configuration", Keyboard.KEY_SUBTRACT, Constants.NAME);

	public WorldTooltips() {
		instance = this;
	}

	@EventHandler
	public void pre(FMLPreInitializationEvent event) {
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile(), Constants.VERSION);
		Config.setConfiguration(cfg);
		Config.populate();
		Config.save();
		ClientRegistry.registerKeyBinding(configKey);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(events);
	}

	@EventHandler
	public void post(FMLPostInitializationEvent event) {
		ModUtils.post();
	}

	@EventHandler
	public void disable(FMLModDisabledEvent event) {
		MinecraftForge.EVENT_BUS.unregister(events);
	}

	@SubscribeEvent
	public void keypress(KeyInputEvent event) {
		if (configKey.isPressed())
			Minecraft.getMinecraft().displayGuiScreen(new GuiConfigTooltips(Minecraft.getMinecraft().currentScreen));
	}

	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event) {
		Config.save();
	}
}
