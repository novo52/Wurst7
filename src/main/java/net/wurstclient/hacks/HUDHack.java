package net.wurstclient.hacks;

import net.wurstclient.Category;
import net.wurstclient.SearchTags;
import net.wurstclient.hack.Hack;
import net.wurstclient.settings.CheckboxSetting;
import net.wurstclient.settings.EnumSetting;

@SearchTags({"HUD"})
public final class HUDHack extends Hack
{
	private final EnumSetting position = new EnumSetting("Position", "Where to render the HUD", Position.values(), Position.LEFT);
	private final CheckboxSetting fps = new CheckboxSetting("FPS", "Display your current FPS", true);
	private final CheckboxSetting speed = new CheckboxSetting("Speed", "Display your current speed", true);
	private final CheckboxSetting coords = new CheckboxSetting("Coordinates", "Display your current coordinates", true);
	private final CheckboxSetting dim_coord = new CheckboxSetting("Dimension Coordinates", "Display your Nether coordinates in the Owerworld or your Owerworld coordinates in the Nether", true);
	
	public HUDHack()
	{
		super("HUD", "Display useful information on your screen");
		setCategory(Category.OTHER);
		addSetting(position);
		addSetting(fps);
		addSetting(speed);
		addSetting(coords);
		addSetting(dim_coord);
	}
	
	public enum Position
	{
		LEFT,
		RIGHT
	}
}