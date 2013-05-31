/**
* PVPGunRealism - a bukkit plugin
* Copyright (C) 2013 dmulloy2
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
package net.dmulloy2.pvpgunrealism;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;

public class PVPGunRealism extends JavaPlugin implements CommandExecutor
{
	@Override
	public void onEnable()
	{
		long start = System.currentTimeMillis();
		
		PluginManager pm = getServer().getPluginManager();
		
		checkPVPGun(pm);
		
		pm.registerEvents(new PVPGunPlusListener(this), this);
		
		getCommand("pvpgunrealism").setExecutor(this);
		
		saveDefaultConfig();
		
		long finish = System.currentTimeMillis();
		
		getLogger().info(getDescription().getFullName() + " has been enabled! ("+(finish-start)+" ms)");
	}
	
	@Override
	public void onDisable()
	{
		long start = System.currentTimeMillis();
		
		long finish = System.currentTimeMillis();
		
		getLogger().info(getDescription().getFullName() + " has been disabled! ("+(finish-start)+" ms)");
	}
	
	public void checkPVPGun(PluginManager pm)
	{
		if (!pm.isPluginEnabled("PVPGunPlus"))
		{
			getLogger().severe("Did not find PVPGunPlus! Disabling!");
			
			onDisable();
			pm.disablePlugin(this);
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (args.length > 0)
		{
			if (args[0].equalsIgnoreCase("reload"))
			{
				if (sender.hasPermission("pvpgunrealism.reload"))
				{
					reloadConfig();
					sender.sendMessage(ChatColor.GOLD + "Reloaded Config!");
				}
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "Invalid args! Try /pvpgunrealism reload!");
			}
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "Invalid args! Try /pvpgunrealism reload!");
		}
		return true;
	}

	public boolean checkFactions(Location loc, boolean safeZoneCheck)
	{
		PluginManager pm = getServer().getPluginManager();
		if (pm.isPluginEnabled("Factions"))
		{
			Plugin pl = pm.getPlugin("Factions");
			String version = pl.getDescription().getVersion();
			if (version.startsWith("1.6."))
			{
				Faction otherFaction = Board.getFactionAt(new FLocation(loc));
				if (safeZoneCheck == true)
				{
					if (otherFaction.isWarZone() || otherFaction.isSafeZone())
						return true;
				}
				else
				{
					if (otherFaction.isWarZone())
						return true;
				}
			}
		}
		if (pm.isPluginEnabled("SwornNations"))
		{
			Faction otherFaction = Board.getFactionAt(new FLocation(loc));
			if (safeZoneCheck == true)
			{
				if (otherFaction.isWarZone() || otherFaction.isSafeZone())
					return true;
			}
			else
			{
				if (otherFaction.isWarZone())
					return true;
			}
		}
		return false;
	}
}