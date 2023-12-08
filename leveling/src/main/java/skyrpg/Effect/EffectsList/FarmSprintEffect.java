package skyrpg.Effect.EffectsList;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.block.data.type.Bed.Part;

import skyrpg.PlayerProfile;
import skyrpg.Effect.SEffect;

public class FarmSprintEffect extends SEffect {
    
    public FarmSprintEffect(int dur, int lvl){
        super(dur, lvl);
        name = "Farm sprint";
    }

    @Override
    public void OnEnable(PlayerProfile player) {
        player.player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(new AttributeModifier("farm_sprint", (float)level/4, Operation.MULTIPLY_SCALAR_1));
    }

    @Override
    public void OnDisable(PlayerProfile player){
        for (AttributeModifier am : player.player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getModifiers()) {
            if(am != null && am.getName().contains("farm_sprint")){
                player.player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(am);
            }
        }
    }

    @Override
    public void OnSecondPass(PlayerProfile player){
        //Bukkit.getServer().broadcastMessage(player.playerName+" has fs" + "; left" + duration);
        player.player.getWorld().spawnParticle(Particle.COMPOSTER, player.player.getLocation(), 1);
    }
}
