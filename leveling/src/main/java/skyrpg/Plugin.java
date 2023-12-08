package skyrpg;

import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import skyrpg.Ability.AbilityGlobal;
import skyrpg.Ability.Abilities.Dash;
import skyrpg.Ability.Abilities.Deflect;
import skyrpg.Ability.Abilities.DoubleJump;
import skyrpg.Ability.Abilities.Experienced;
import skyrpg.Ability.Abilities.FarmSprint;
import skyrpg.Ability.Abilities.GoldDigger;
import skyrpg.Ability.Abilities.MeleeTechnique;
import skyrpg.Ability.Abilities.TheLuckyOne;
import skyrpg.Ability.Abilities.Vitality;

/*
 * leveling java plugin
 */
public class Plugin extends JavaPlugin
{
  PlayerEventListener PlEvL;
  private static final Logger LOGGER=Logger.getLogger("leveling");
  public Dictionary<String,PlayerProfile> profiles;

  protected PluginSaver pluginSaver;


  public void onEnable()
  {
    pluginSaver = new PluginSaver(this);
    
    profiles = new Hashtable<>();

    PlEvL = new PlayerEventListener(this);
    Bukkit.getServer().getPluginManager().registerEvents(PlEvL, this);
    //Bukkit.getServer().broadcastMessage("[SkyRPG - leveling]: enabled");
    
    AbilitiesInit();
    LOGGER.info("[SkyRPG - leveling]: ENABLED");

    File dataFolder = new File(getDataFolder(), "data");
    dataFolder.mkdirs();
  }

  


  //------ abilities -------------------------------
  List<AbilityGlobal> abilityList;

  public void AbilitiesInit(){
    LOGGER.info("[SkyRPG - leveling]: Abilities init...");
    abilityList = new ArrayList<>();
    
    abilityList.add(new Vitality());
    abilityList.add(new Dash());
    abilityList.add(new DoubleJump());

    abilityList.add(new MeleeTechnique());
    abilityList.add(new Deflect());

    abilityList.add(new FarmSprint(this));

    abilityList.add(new GoldDigger());

    abilityList.add(new TheLuckyOne());

    abilityList.add(new Experienced());


    LOGGER.info("[SkyRPG - leveling]: ____Global abilities___");
    for (AbilityGlobal ag : abilityList) {
      LOGGER.info(ag.name);
    }
  } 


  //------ saving and loading progress -------------------------------

    public void LoadPlayer(PlayerJoinEvent event)
  {
    Bukkit.getServer().broadcastMessage("Player joined!");
    Player p = event.getPlayer();

    Object opp = pluginSaver.load(pluginSaver.profilesDir+"/"+p.getName()+".dat");
    PlayerProfile profile;

    if(opp==null){
      Bukkit.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE+"Welcome, new player, "+ChatColor.WHITE+p.getName());
      profile = new PlayerProfile(this, p);
    }
    else{
      profile = (PlayerProfile)opp;
      profile.plugin = this;
      profile.player = p;
      profile.playerName = profile.player.getName();
      //profile.abilities = new ArrayList<>();
      profile.AbilitiesInit();
      profile.ClearEffects();
      profile.EffectsSecondPass();
      Bukkit.getServer().broadcastMessage(ChatColor.DARK_PURPLE+"Welcome back, "+ChatColor.WHITE+p.getName());
    }

    profiles.put(p.getName(), profile);
    Bukkit.getServer().broadcastMessage(profile.GetInfo());
  }


  public void SavePlayer(PlayerQuitEvent event)
  {
    Bukkit.getServer().broadcastMessage("Player quit!");
    Player p = event.getPlayer();
    PlayerProfile profile = profiles.get(p.getName());
    profile.ClearEffects();
    profile.SaveAbilitiesToDic();

    pluginSaver.save(profile, new File(pluginSaver.profilesDir+"/"+p.getName()+".dat"));

    profiles.remove(p.getName());
  }
}
