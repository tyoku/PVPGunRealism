package net.dmulloy2.pvpgunrealism;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import com.orange451.pvpgunplus.events.PVPGunPlusBulletCollideEvent;
import com.orange451.pvpgunplus.events.PVPGunPlusGunDamageEntityEvent;

public class PVPGunPlusListener implements Listener
{
	public PVPGunRealism plugin;
	public PVPGunPlusListener(PVPGunRealism plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBulletCollideWithBlock(PVPGunPlusBulletCollideEvent event)
	{
		if (event.isCancelled())
			return;
		
		Player player = event.getShooterAsPlayer();
		if (player == null)
			return;
		
		Block block = event.getBlockHit();
		if (block == null)
			return;
		
		Material mat = block.getState().getType();
		
		List<Material> materials = new ArrayList<Material>();
		
		List<String> configMaterials = plugin.getConfig().getStringList("block-shatter.blocks");
		for (String configMaterial : configMaterials)
		{
			Material material = null;
			try { material = Material.getMaterial(configMaterial.toUpperCase()); }
			catch (Exception e) { material = Material.getMaterial(Integer.parseInt(configMaterial)); }
			if (material == null)
			{
				plugin.getLogger().severe("Error parsing material \"" + configMaterial + "\"!");
			}
			else
			{
				materials.add(material);
			}
		}

		for (Material material : materials)
		{
			if (material == mat)
			{
				BlockBreakEvent blockBreak = new BlockBreakEvent(block, player);
				if (!blockBreak.isCancelled())
				{
					block.breakNaturally();
				}
			}
		}
		
		if (plugin.getConfig().getBoolean("block-crack"))
		{
			if (mat == Material.STONE)
			{
				block.setType(Material.COBBLESTONE);
			}
			if (mat == Material.SMOOTH_BRICK)
			{
				block.setData((byte) 2);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onGunDamageEntity(PVPGunPlusGunDamageEntityEvent event)
	{
		if (event.isCancelled())
			return;
		
		Entity entity = event.getEntityDamaged();
		if (entity == null)
			return;

		World world = entity.getWorld();
		
		if (plugin.getConfig().getBoolean("blood-effect"))
		{
			entity.getWorld().playEffect(entity.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK.getId());
		}
		
		if (plugin.getConfig().getBoolean("smoke-effect"))
		{
			entity.getWorld().playEffect(event.getShooter().getPlayer().getLocation(), Effect.SMOKE, 5);
		}
		
		if (plugin.getConfig().getBoolean("bullet-sound.enabled"))
		{
			Sound sound = Sound.valueOf(plugin.getConfig().getString("bullet-sound.sound").toUpperCase());
			world.playSound(entity.getLocation(), sound, 10, 1);
		}
	}
}
