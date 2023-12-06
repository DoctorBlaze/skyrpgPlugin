package skyrpg.Ability.Abilities;

import java.util.ArrayList;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import skyrpg.Ability.AbilityGlobal;
import skyrpg.Skill.SkillType;

public class Dash extends AbilityGlobal {
    public Dash(){
        name = "Dash";
        desc = new ArrayList<>();
        desc.add(ChatColor.GRAY+"Increases your speed");
        desc.add(ChatColor.GRAY+"for short period of time");
        desc.add(ChatColor.DARK_GRAY + "Cooldown: 1.5s");
        desc.add(ChatColor.GRAY+"");

        skillType = SkillType.general;
        neededLevel = 2;

        fullCooldown = 30;

        maxLevel = 16;

    }

    @Override
    public void UseAbility(Player player,int lvl){
        float power = (16+lvl)/16;
        player.getWorld().spawnParticle(Particle.CLOUD,player.getLocation(),4);
        player.setVelocity(player.getLocation().getDirection().multiply(new Vector(power, 0.0, power)));
    }

}
