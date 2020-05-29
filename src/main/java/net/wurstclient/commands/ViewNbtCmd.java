/*
 * Copyright (C) 2014 - 2020 | Alexander01998 | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.commands;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.wurstclient.SearchTags;
import net.wurstclient.command.CmdError;
import net.wurstclient.command.CmdException;
import net.wurstclient.command.CmdSyntaxError;
import net.wurstclient.command.Command;
import net.wurstclient.util.ChatUtils;
import net.wurstclient.util.MathUtils;

@SearchTags({"view nbt", "NBTViewer", "nbt viewer"})
public final class ViewNbtCmd extends Command
{
	public ViewNbtCmd()
	{
		super("viewnbt",
			"Allows you to see the NBT tags of TileEntities\n"
				+ "or items in your hand.",
			".viewnbt print <x> <y> <z>", ".viewnbt copy <x> <y> <z>",
			".viewnbt tilelist", ".viewnbt inv (print|copy)");
	}

	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length == 1 && args[0].equalsIgnoreCase("tilelist"))
		{
			int count = 0;
			for(BlockEntity be : MC.world.blockEntities)
			{
				ChatUtils.message(be.getPos().toString());
				count++;
			}
			ChatUtils.message("Total entities: " + count);
		}else if(args.length == 4 && (args[0].equalsIgnoreCase("print") || args[0].equalsIgnoreCase("copy")) && 
			MathUtils.isInteger(args[1]) && MathUtils.isInteger(args[2]) && MathUtils.isInteger(args[3]))
		{
			int x = Integer.parseInt(args[1]);
			int y = Integer.parseInt(args[2]);
			int z = Integer.parseInt(args[3]);
			for(BlockEntity be : MC.world.blockEntities)
			{
				if(be.getPos().getX() == x && be.getPos().getY() == y &&
					be.getPos().getZ() == z)
				{
					CompoundTag tag = be.toTag(new CompoundTag());
					if(args[0].equalsIgnoreCase("print"))
						ChatUtils.message("Data: " + tag.toString());
					else if(args[0].equalsIgnoreCase("copy"))
					{
						MC.keyboard.setClipboard(tag.toString());
						ChatUtils.message("Data copied to clipboard.");
					}
					return;
				}
			}
			throw new CmdError("TileEntity not found at " + x + ", " + y + ", " + z + "!");
		}else if(args.length == 2 && args[0].equalsIgnoreCase("inv") && (args[1].equalsIgnoreCase("print")
			|| args[1].equalsIgnoreCase("copy")))
		{
			CompoundTag tag = new CompoundTag();
			ItemStack item = MC.player.inventory.getMainHandStack();
			if(!item.isEmpty())
			{
				item.setTag(tag);
				if(args[1].equalsIgnoreCase("print"))
					ChatUtils.message("Data: " + tag.toString());
				else
				{
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
						new StringSelection(tag.toString()), null);
					ChatUtils.message("NBT data copied to clipboard.");
				}
			}else
				ChatUtils.message("You must hold an item in your main hand.");
		}else
			throw new CmdSyntaxError();
	}
}
