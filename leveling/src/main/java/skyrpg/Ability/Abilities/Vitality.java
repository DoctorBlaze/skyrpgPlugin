package skyrpg.Ability.Abilities;

import java.util.ArrayList;

import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import skyrpg.Ability.AbilityGlobal;
import skyrpg.Skill.SkillType;

public class Vitality extends AbilityGlobal {
    public Vitality(){
        name = "Vitality";
        desc = new ArrayList<>();
        desc.add(ChatColor.GRAY+"Increases your max HP");
        desc.add(ChatColor.GRAY+"for 1 point per level");
        desc.add(ChatColor.DARK_GRAY + "Passive ability");
        desc.add(ChatColor.GRAY+"");

        skillType = SkillType.general;
        neededLevel = 4;

        fullCooldown = 60;

        maxLevel = 10;

        autocastAbility = true;

    }

    @Override
    public void UseAbility(Player player,int lvl){
        //player.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(new AttributeModifier("vitality_effect", 0, Operation.ADD_NUMBER));
        
        for (AttributeModifier am : player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers()) {
            if(am != null && am.getName().contains("vitality_effect")){
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).removeModifier(am);
            }
        }
        if(lvl < 1) return;
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(new AttributeModifier("vitality_effect", lvl, Operation.ADD_NUMBER));

        /*Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "attribute "+player.getName()+" minecraft:generic.max_health modifier remove ca263318-9511-11ee-b9d1-0242ac120002");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "attribute "+player.getName()+" minecraft:generic.max_health modifier add ca263318-9511-11ee-b9d1-0242ac120002 vit_boost "+lvl+" add"); */
        
    }

}
