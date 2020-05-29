/*
 * Copyright (C) 2014 - 2020 | Alexander01998 | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.commands;

import net.minecraft.SharedConstants;
import net.wurstclient.WurstClient;
import net.wurstclient.command.CmdError;
import net.wurstclient.command.CmdException;
import net.wurstclient.command.CmdSyntaxError;
import net.wurstclient.command.Command;
import net.wurstclient.util.ChatUtils;
import net.wurstclient.util.LastServerRememberer;

public final class InfoCmd extends Command
{
	public InfoCmd()
	{
		super("info", "Gives you information about either the client or the server.",
			"(client|server)");
	}

	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length == 1)
		{
			if(args[0].equalsIgnoreCase("client"))
			{
				String version =
					"Version: " + WurstClient.MC_VERSION;
				String protocolversion = "Protocol Version: "
					+ SharedConstants.getGameVersion().getProtocolVersion();
				String name = "Name: " + MC.getSession().getProfile().getName();
				String sessionid =
					"Session id: " + MC.getSession().getSessionId();
				String uuid = "UUID: " + MC.player.getUuid();
				ChatUtils.message(version);
				ChatUtils.message(protocolversion);
				ChatUtils.message(name);
				ChatUtils.message(sessionid);
				ChatUtils.message(uuid);
			}else if(args[0].equalsIgnoreCase("server"))
			{
				if(LastServerRememberer.getLastServer() == null)
					throw new CmdError("You haven't joined a server!");
				String version = "Server version: "
					+ LastServerRememberer.getLastServer().version;
				String ping =
					"Ping: " + LastServerRememberer.getLastServer().ping;
				String list =
					"Players: " + LastServerRememberer.getLastServer().playerListSummary;
				String slots = "Player Slots: "
					+ LastServerRememberer.getLastServer().playerCountLabel;
				String ip = "IP: " + LastServerRememberer.getLastServer().address;
				String motd =
					"MOTD: " + LastServerRememberer.getLastServer().label;
				String name =
					"Server Name: " + LastServerRememberer.getLastServer().name;
				String brand = 
					"Server Brand: " + MC.player.getServerBrand();
				String protocolversion = "Protocol version: "
					+ LastServerRememberer.getLastServer().protocolVersion;
				ChatUtils.message(version);
				ChatUtils.message(ping);
				ChatUtils.message(list);
				ChatUtils.message(slots);
				ChatUtils.message(ip);
				ChatUtils.message(motd);
				ChatUtils.message(name);
				ChatUtils.message(brand);
				ChatUtils.message(protocolversion);
			}else
				throw new CmdSyntaxError();
		}else
			throw new CmdSyntaxError();
	}
}
