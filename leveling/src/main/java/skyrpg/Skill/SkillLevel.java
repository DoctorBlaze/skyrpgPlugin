package skyrpg.Skill;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import skyrpg.PlayerProfile;

public class SkillLevel implements Serializable {

    public PlayerProfile owner;

    public SkillType skillType;

    public int level;
    public int score;

    //construct ---------------------------------------------------------------------
    public SkillLevel(PlayerProfile owner, SkillType skillType){
        this.owner = owner;
        this.skillType = skillType;
        level=0;
        score=0;
    }

    public SkillLevel(int l, int s,PlayerProfile owner, SkillType skillType){
        this.owner = owner;
        this.skillType = skillType;
        level=l;
        score=s;
    }
    //-------------------------------------------------------------------------------

    public void ReloadSkill(){
        level=0;
        score=0;
    }

    public int GetNeededScore(){
        return ((level*level*(int)Math.sqrt(level))+1)*50;
    }

    public void AddScore(int value){
        //if(skillType == SkillType.combat) Bukkit.getServer().dispatchCommand(owner.player,"execute as "+owner.player.getName()+" run summon item ^ ^1 ^2 {CustomNameVisible:1b,Age:5980,Health:100,PickupDelay:32767,Motion:[0.0,0.2,0.0],Tags:[\"display_item\"],CustomName:'[{\"text\":\"+"+String.valueOf(value)+"\",\"color\":\"white\"},{\"text\":\"🗡\",\"color\":\"red\"}]',Item:{id:\"minecraft:iron_sword\",Count:1b}}");

        score = score+value;
        if(score > GetNeededScore()){
            level+=1;
            owner._OnSkillLevelup(this);
        }
    }

    public String GetInfo(){
        String ret = skillType.toString()+" level: " + String.valueOf(level) + 
        "  \t[Score: " + String.valueOf(score) + "/" + String.valueOf(GetNeededScore())+"]";
        return ret;
    }

    public List<String> GetListInfo(){
        List<String> ret = new ArrayList<String>();

        ret.add(ChatColor.WHITE+"level: " + String.valueOf(level));
        ret.add(ChatColor.GRAY+"" + String.valueOf(score) + "/" + String.valueOf(GetNeededScore())+"");

        String a = "";
        for(int i = 0; i < 10; ++i){
            if(score*100/GetNeededScore() > i*10){
                a+=ChatColor.GREEN+"▬";
            }
            else{
                a+=ChatColor.DARK_GRAY+"▭";
            }
        }
        
        ret.add(ChatColor.GREEN+a);

        return ret;
    }


}