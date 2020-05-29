/*
 * Copyright (C) 2014 - 2020 | Alexander01998 | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import net.wurstclient.command.CmdException;
import net.wurstclient.command.CmdSyntaxError;
import net.wurstclient.command.Command;
import net.wurstclient.util.ChatUtils;

public final class NameHistoryCmd extends Command
{
	public NameHistoryCmd()
	{
		super("namehistory", "Shows a player's name history using their name or UUID.",
			"[(name|UUID) <nameIn|UUIDIn>]");
	}

	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length == 2 && args[0].equalsIgnoreCase("name")
			|| args[0].equalsIgnoreCase("uuid"))
		{
			String uuid = "";
			if(args[0].equalsIgnoreCase("name"))
				try
				{
					URL url = new URL(
						"https://api.mojang.com/users/profiles/minecraft/"
							+ args[0]);
					BufferedReader br = new BufferedReader(
						new InputStreamReader(url.openStream()));
					String line;
					while((line = br.readLine()) != null)
						uuid = line.split("\"")[3];
					br.close();
				}catch(IOException e)
				{
					ChatUtils.error("Unable to retrieve name of the player.");
				}
			else
				uuid = args[1];
			try
			{
				URL url = new URL(
					"https://api.mojang.com/user/profiles/" + uuid + "/names");
				BufferedReader br =
					new BufferedReader(new InputStreamReader(url.openStream()));
				String originalName;
				String oldNames = "";
				String line;
				while((line = br.readLine()) != null)
				{
					originalName = line.split("\"")[3];
					for(int i = 0; i < line.split("\"").length; i++)
						if(i != line.split("\"").length - 1
							&& line.split("\"")[i + 1].equals(","))
							if(oldNames.equals(""))
								oldNames = line.split("\"")[i];
							else
								oldNames =
									oldNames + ", " + line.split("\"")[i];
					if(oldNames.equals(""))
						ChatUtils
							.message(args[0] + " hasn't changed their name.");
					else
						ChatUtils.message(args[0] + "'s name history: "
							+ originalName + ", " + oldNames + ".");
				}
			}catch(IOException e)
			{
				ChatUtils.error("Unable to retrieve past names of the player.");
			}
		}else
			throw new CmdSyntaxError();
	}
}
