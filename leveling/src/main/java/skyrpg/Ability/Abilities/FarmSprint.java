package skyrpg.Ability.Abilities;

import java.util.ArrayList;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import skyrpg.PlayerProfile;
import skyrpg.Plugin;
import skyrpg.Ability.AbilityGlobal;
import skyrpg.Effect.EffectsList.FarmSprintEffect;
import skyrpg.Skill.SkillType;

public class FarmSprint extends AbilityGlobal {
    public FarmSprint(Plugin plg){
        name = "Farm Sprint";
        desc = new ArrayList<>();
        desc.add(ChatColor.GRAY+"Increases your speed");
        desc.add(ChatColor.GRAY+"after collecting a crop for 20 seconds");
        desc.add(ChatColor.DARK_GRAY + "Cooldown: 20.0s");
        desc.add(ChatColor.GRAY+"");

        skillType = SkillType.farming;
        neededLevel = 2;

        fullCooldown = 400;

        maxLevel = 8;
        plugin = plg;

    }

    public Plugin plugin;

    @Override
    public void UseAbility(Player player,int lvl){
        plugin.profiles.get(player.getName()).ApplyEffect(new FarmSprintEffect(20, lvl));
    }

}
