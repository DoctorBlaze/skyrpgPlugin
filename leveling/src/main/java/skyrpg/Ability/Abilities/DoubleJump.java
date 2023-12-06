package skyrpg.Ability.Abilities;

import java.util.ArrayList;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import skyrpg.Ability.AbilityGlobal;
import skyrpg.Skill.SkillType;

public class DoubleJump extends AbilityGlobal {
    public DoubleJump(){
        name = "Double Jump";
        desc = new ArrayList<>();
        desc.add(ChatColor.GRAY+"You can make one more jump");
        desc.add(ChatColor.GRAY+"while mid-air");
        desc.add(ChatColor.DARK_GRAY + "Cooldown: 3s");
        desc.add(ChatColor.GRAY+"");

        skillType = SkillType.general;
        neededLevel = 3;

        fullCooldown = 60;

        maxLevel = 12;

    }

    @Override
    public void UseAbility(Player player,int lvl){
        float power = (24+lvl)/12;
        player.getWorld().spawnParticle(Particle.CLOUD,player.getLocation(),4);
        player.setVelocity((player.getLocation().getDirection().multiply(new Vector(power*0.25, 0, power*0.25)).add(new Vector(0, power/5, 0))));
    }

}
