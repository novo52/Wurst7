package net.wurstclient.hud;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.math.Position;
import net.minecraft.world.dimension.DimensionType;
import net.wurstclient.WurstClient;
import net.wurstclient.hacks.HUDHack;
import net.wurstclient.other_features.HackListOtf;
import net.wurstclient.settings.CheckboxSetting;
import net.wurstclient.settings.EnumSetting;
import net.wurstclient.settings.Setting;

import java.util.Map;

public class InfoHUD
{
	private double speed;
	private Position prevPos;
	private long prevTime;
	private Map<String, Setting> settings;
	
	public InfoHUD()
	{
		settings = WurstClient.INSTANCE.getHax().hudHack.getSettings();
		prevTime = System.currentTimeMillis();
		speed = 0;
		prevPos = new Position()
		{
			
			@Override
			public double getX()
			{
				return 0;
			}
			
			@Override
			public double getY()
			{
				return 0;
			}
			
			@Override
			public double getZ()
			{
				return 0;
			}
		};
	}
	
	public void renderer(float partialTicks)
	{
		Position pos = WurstClient.MC.player.getPos();
		
		int currentLayer = 1;
		
		boolean inNether = WurstClient.MC.player.dimension == DimensionType.THE_NETHER;
		
		int white = 0xffffffff;
		int red = 0xffff0000;
		int dimensionColor = inNether ? white : red;
		int normalColor = 	!inNether ? white : red;
		
		if(((CheckboxSetting) settings.get("dimension coordinates")).isChecked())
		{
			float multiplier = inNether ? 8 : 1.25F;
			String dimension_pos = String.format("X: %d, Y: %d, Z: %d", (int) (pos.getX() * multiplier), (int) (pos.getY()), (int) (pos.getZ() * multiplier));
			drawText(dimension_pos, currentLayer++, dimensionColor);
		}
		
		if(((CheckboxSetting) settings.get("coordinates")).isChecked())
		{
			String defpos = String.format("X: %d, Y: %d, Z: %d", (int) pos.getX(), (int) pos.getY(), (int) pos.getZ());
			drawText(defpos, currentLayer++, normalColor);
		}
		// TODO: Calculate the speed more accurately
		if(((CheckboxSetting) settings.get("speed")).isChecked())
		{
			long currTime = System.currentTimeMillis();
			long deltaTime = currTime - prevTime;
			if(deltaTime > 500)
			{
				double dist = Math.sqrt(Math.pow(pos.getX() - prevPos.getX(), 2) + Math.pow(pos.getZ() - prevPos.getZ(), 2));
				speed = dist / ((double) deltaTime / 1000);
				prevPos = pos;
				prevTime = currTime;
			}
			drawText(Math.round(speed * 10.0) / 10.0 + " m/s", currentLayer++, 0xffffffff);
		}
		if(((CheckboxSetting) settings.get("fps")).isChecked())
			drawText(WurstClient.MC.fpsDebugString.split(" fps")[0] + " FPS", currentLayer, 0xffffffff);
	}
	
	public void drawText(String s, int layer, int color)
	{
		TextRenderer tr = WurstClient.MC.textRenderer;
		
		int posX;
		int posY;
		
		int screenWidth = WurstClient.MC.getWindow().getScaledWidth();
		int screenHeight = WurstClient.MC.getWindow().getScaledHeight();
		
		int layerOffset = 10 * layer;
		posY = screenHeight - layerOffset - 2;
		
		EnumSetting hudPosition = (EnumSetting) settings.get("position");
		if(hudPosition.getSelected() == HUDHack.Position.LEFT)
			posX = 2;
		else
		{
			int stringWidth = tr.getStringWidth(s);
			posX = screenWidth - stringWidth - 2;
		}
		
		tr.draw(s, posX, posY, color);
	}
}