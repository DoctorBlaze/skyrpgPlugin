package skyrpg.Ability;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import skyrpg.PlayerProfile;

public class AbilityPlayer {
    public PlayerProfile owner;

    public AbilityGlobal ability;

    public boolean visible;

    public int level;

    public boolean cooldown;

    public boolean UpgradeAbility(){
        if(visible && level < ability.maxLevel){
            level++;
            return true;
        }
        return false;
    }

    public void UseAbility(Player player){
        if(level <= 0 || cooldown) return;
        cooldown = true;
        new BukkitRunnable(){public void run(){cooldown=false;}}.runTaskLater(owner.plugin, ability.fullCooldown);
        ability.UseAbility(player, level);
        
    }
}
