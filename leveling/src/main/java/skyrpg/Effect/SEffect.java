package skyrpg.Effect;

import org.bukkit.scheduler.BukkitRunnable;

import skyrpg.PlayerProfile;
import skyrpg.Plugin;

public class SEffect {
    public String name;
    public int level;
    public int duration;


    public SEffect(int dur, int lvl){
        duration = dur;
        level = lvl;
    }


    public void OnEnable(PlayerProfile player){}
    public void OnDisable(PlayerProfile player){}
    public void OnSecondPass(PlayerProfile player){

    }
    
}
