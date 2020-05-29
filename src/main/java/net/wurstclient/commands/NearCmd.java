/*
 * Copyright (C) 2014 - 2020 | Alexander01998 | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.wurstclient.command.CmdException;
import net.wurstclient.command.CmdSyntaxError;
import net.wurstclient.command.Command;
import net.wurstclient.util.ChatUtils;

public final class NearCmd extends Command
{
	public NearCmd()
	{
		super("near",
			"Gets a list of the players in your area. You can\n"
				+ "choose the order (e.g. closest means that closest players come first).\n"
				+ "Note that this only displays players the client is notified about.",
				"[closest|farthest]");
	}

	@Override
	public void call(String[] args) throws CmdException
	{	
		if(args.length != 1)
			throw new CmdSyntaxError();
		if(!args[0].equalsIgnoreCase("closest") && !args[0].equalsIgnoreCase("farthest"))
			throw new CmdSyntaxError();
		List<OtherClientPlayerEntity> playerList = new ArrayList<>();
		for(AbstractClientPlayerEntity e : MC.world.getPlayers())
			if(e instanceof OtherClientPlayerEntity)
			{
				OtherClientPlayerEntity en = (OtherClientPlayerEntity)e;
				playerList.add(en);
			}
		if(args[0].equalsIgnoreCase("closest"))
			Collections.sort(playerList, new Comparator<OtherClientPlayerEntity>() 
	      		{
	      			@Override
	      			public int compare(OtherClientPlayerEntity o1, OtherClientPlayerEntity o2)
	      			{
	      				return Float.valueOf(MC.player.distanceTo(o1)).compareTo(
	      					MC.player.distanceTo(o2));
	      			}
	      		});
			else
				Collections.sort(playerList, new Comparator<OtherClientPlayerEntity>() 
	      		{
	      			@Override
	      			public int compare(OtherClientPlayerEntity o1, OtherClientPlayerEntity o2)
	      			{
	      				return Float.valueOf(MC.player.distanceTo(o2)).compareTo(
	      					MC.player.distanceTo(o1));
	      			}
	      		});
		StringBuilder playerBuilder = new StringBuilder("\u00a76There are \u00a7c" + playerList.size() 
			+ " \u00a76players near:\u00a7r ");
		playerList.forEach(player -> playerBuilder.append(player.getName()).append(", "));
		if(playerList.size() > 0 && !playerBuilder.toString().equals(""))
			ChatUtils.message(playerBuilder.toString().substring(0, playerBuilder.toString().length() - 2));
		else
			ChatUtils.message("Could not find any near players!");
	}
}
