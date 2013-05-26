package net.dmulloy2.pvpgunrealism;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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
}