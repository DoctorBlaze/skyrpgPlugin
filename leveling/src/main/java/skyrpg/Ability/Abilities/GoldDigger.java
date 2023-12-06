package skyrpg.Ability.Abilities;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import skyrpg.Ability.AbilityGlobal;
import skyrpg.Skill.SkillType;

public class GoldDigger extends AbilityGlobal {
    public GoldDigger(){
        name = "Gold Digger";
        desc = new ArrayList<>();
        desc.add(ChatColor.GRAY+"Increases your speed");
        desc.add(ChatColor.GRAY+"for short period of time");
        desc.add(ChatColor.GRAY+"");

        skillType = SkillType.building;
        neededLevel = 5;

        fullCooldown = 0;

        maxLevel = 20;

    }

    @Override
    public void UseAbility(Player player,int lvl){
        player.setVelocity(player.getEyeLocation().getDirection().multiply((float)lvl/4.0f));
    }

}
