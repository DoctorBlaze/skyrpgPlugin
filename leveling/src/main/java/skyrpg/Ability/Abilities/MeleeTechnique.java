package skyrpg.Ability.Abilities;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import skyrpg.Ability.AbilityGlobal;
import skyrpg.Skill.SkillType;

public class MeleeTechnique extends AbilityGlobal {
    public MeleeTechnique(){
        name = "Melee technique";
        desc = new ArrayList<>();
        desc.add(ChatColor.GRAY+"Increases your melee damage");
        //desc.add(ChatColor.GRAY+"while mid-air");
        desc.add(ChatColor.GRAY+"");

        skillType = SkillType.combat;
        neededLevel = 4;

        fullCooldown = 0;

        maxLevel = 24;

    }

    @Override
    public void UseAbility(Player player,int lvl){
        player.setVelocity(player.getEyeLocation().getDirection().multiply((float)lvl/4.0f));
    }

}
