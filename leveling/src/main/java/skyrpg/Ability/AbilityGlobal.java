package skyrpg.Ability;

import java.util.List;

import org.bukkit.entity.Player;

import skyrpg.Plugin;
import skyrpg.Skill.SkillType;

public abstract class AbilityGlobal {
    public String name;
    public List<String> desc;

    public SkillType skillType;
    public int neededLevel;

    public int fullCooldown;

    public int maxLevel;

    public boolean autocastAbility=false;

    public Plugin plg;

    public void UseAbility(Player player, int lvl){

    }

}
