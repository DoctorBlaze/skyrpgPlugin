package skyrpg;

import skyrpg.Ability.AbilityGlobal;
import skyrpg.Ability.AbilityPlayer;
import skyrpg.Ability.Abilities.Dash;
import skyrpg.Skill.*;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.CauldronLevelChangeEvent.ChangeReason;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;


public class PlayerProfile implements Serializable{

    public int abilityPoints;

    transient public Plugin plugin;
    transient public Player player;
    transient public String playerName;

    public Dictionary<String,Integer> abilitySavedLevels; 
    
    SkillType displayedSkill = SkillType.general;

    public PlayerProfile(Plugin pl,Player plr){
        abilityPoints = 80;
        plugin = pl;
        player = plr;
        playerName = plr.getName();
        combatLevel = new SkillLevel(this, SkillType.combat);
        buildingLevel = new SkillLevel(this, SkillType.building);
        fishingLevel = new SkillLevel(this, SkillType.fishing);
        farmingLevel = new SkillLevel(this, SkillType.farming);
        alchemyLevel = new SkillLevel(this, SkillType.alchemy);
        
        AbilitiesInit();

        //plugin.getLogger().info(playerName);
        //plugin.getLogger().info(combatLevel.GetInfo());
    }


    public String GetInfo(){
        String ret = "Player: " + playerName + 
        "\n-" + combatLevel.GetInfo() + 
        "\n-" + buildingLevel.GetInfo() + 
        "\n-" + fishingLevel.GetInfo() + 
        "\n-" + farmingLevel.GetInfo() + 
        "\n-" + alchemyLevel.GetInfo();
        return ret;
    }
    


    public int generalLevel;

    public void UpdGeneralLevel(){
        generalLevel = combatLevel.level+
        buildingLevel.level+
        fishingLevel.level+
        farmingLevel.level+
        alchemyLevel.level;
    }


    public SkillLevel combatLevel;

    public SkillLevel buildingLevel;

    public SkillLevel fishingLevel;

    public SkillLevel farmingLevel;

    public SkillLevel alchemyLevel;

    transient List<AbilityPlayer> abilities;


    public void _OnSkillLevelup(SkillLevel skill){
        Bukkit.getServer().broadcastMessage(playerName+ChatColor.GOLD+" has got "+skill.skillType.toString() + " level "+(skill.level));
        abilityPoints+=skill.level/2 + 1;
        UpdGeneralLevel();
    }

    //-------------------------------------------------

    public void ViewProfile(){
        Inventory inv = Bukkit.createInventory(player, 27, "Skills");
        for (int i = 0; i < 27; ++i) {
            inv.clear(i);
        }

        ItemStack general_item = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta general_meta = general_item.getItemMeta();
        general_meta.setDisplayName(ChatColor.WHITE + "General");
        
        general_meta.setLore(null);
        general_item.setItemMeta(general_meta);


        ItemStack combat_item = new ItemStack(Material.REDSTONE);
        ItemMeta combat_meta = combat_item.getItemMeta();
        combat_meta.setDisplayName(ChatColor.RED + "Combat");
        combat_meta.setLore(combatLevel.GetListInfo());
        combat_item.setItemMeta(combat_meta);

        ItemStack building_item = new ItemStack(Material.IRON_INGOT);
        ItemMeta building_meta = building_item.getItemMeta();
        building_meta.setDisplayName(ChatColor.GRAY + "Building");
        building_meta.setLore(buildingLevel.GetListInfo());
        building_item.setItemMeta(building_meta);

        ItemStack farming_item = new ItemStack(Material.WHEAT);
        ItemMeta farming_meta = farming_item.getItemMeta();
        farming_meta.setDisplayName(ChatColor.DARK_GREEN + "Farming");
        farming_meta.setLore(farmingLevel.GetListInfo());
        farming_item.setItemMeta(farming_meta);

        ItemStack fishing_item = new ItemStack(Material.FISHING_ROD);
        ItemMeta fishing_meta = fishing_item.getItemMeta();
        fishing_meta.setDisplayName(ChatColor.AQUA + "Fishing");
        fishing_meta.setLore(fishingLevel.GetListInfo());
        fishing_item.setItemMeta(fishing_meta);

        ItemStack alchemy_item = new ItemStack(Material.BOOK);
        ItemMeta alchemy_meta = alchemy_item.getItemMeta();
        alchemy_meta.setDisplayName(ChatColor.DARK_PURPLE + "Alchemy");
        alchemy_meta.setLore(alchemyLevel.GetListInfo());
        alchemy_item.setItemMeta(alchemy_meta);

        inv.setItem(0, general_item);
        inv.setItem(1, combat_item);
        inv.setItem(2, building_item);
        inv.setItem(3, farming_item);
        inv.setItem(4, fishing_item);
        inv.setItem(5, alchemy_item);

        _PrintAbilities(inv, SkillType.general);

        player.openInventory(inv);
    }
    

    public void _PrintAbilities(Inventory inv, SkillType skillType){
        inv.clear(8);
        UpdAbilityVisible(skillType);

        if(abilityPoints > 0){
            ItemStack apItem = new ItemStack(Material.EXPERIENCE_BOTTLE);
            apItem.setAmount(abilityPoints);
            ItemMeta apMeta = apItem.getItemMeta();
            apMeta.setDisplayName(ChatColor.DARK_GREEN + "Ability points: "+ ChatColor.WHITE + abilityPoints);
            apItem.setItemMeta(apMeta);
            inv.setItem(8, apItem);
        }
        
        displayedSkill = skillType;
        //Bukkit.getServer().broadcastMessage("Now displaying: "+skillType.toString());        
        for (int i = 9; i < 27; ++i) {
            inv.clear(i);
        }

        int ind = 9;
        for (AbilityPlayer a : abilities) {
            if(ind >= 27) break;
            if(a.ability.skillType==skillType){
                ItemStack display;
                if(a.level >= a.ability.maxLevel) display = new ItemStack(Material.FIRE_CHARGE);
                else if(a.level > 0) display = new ItemStack(Material.SNOWBALL);
                else if(a.visible) display = new ItemStack(Material.FIREWORK_STAR);
                else display = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);

                ItemMeta display_meta = display.getItemMeta();
                display_meta.setDisplayName(a.ability.name);
                List<String> desc;

                if(a.visible){
                    desc = new ArrayList<>(a.ability.desc);
                    ChatColor ldc=ChatColor.GOLD;
                    if(a.level<=0) ldc = ChatColor.DARK_RED;
                    else if(a.level< a.ability.maxLevel) ldc = ChatColor.DARK_GREEN;
                    else if(a.level< a.ability.maxLevel) ldc = ChatColor.GOLD;
                    int cost = (int)Math.sqrt(a.level)+1;
                    desc.add(ldc+"lvl: ["+a.level+"/"+a.ability.maxLevel+"]");
                    if(a.level< a.ability.maxLevel && abilityPoints >= cost) desc.add(ChatColor.DARK_GREEN+"Click to upgrade ("+cost+" points)");
                    display_meta.setLore(desc);
                }
                else{
                    desc = new ArrayList<>();
                    ChatColor ldc=ChatColor.GRAY;
                    desc.add(ldc+"Need "+a.ability.skillType.toString() +" level: "+a.ability.neededLevel);
                    display_meta.setLore(desc);
                }

                
                display.setItemMeta(display_meta);
                inv.setItem(ind, display);
                ++ind;
            }
        }
    }

    //-------------------------------------------------


    public void AbilitiesInit(){
        abilities = new ArrayList<>();
        Bukkit.getServer().broadcastMessage("____Player abilities___");
        AbilityPlayer a;
        for (AbilityGlobal ag : plugin.abilityList) {
            a = new AbilityPlayer();
            a.ability = ag;
            a.level = 0;
            a.visible = true;
            a.cooldown = false;
            a.owner = this;
            if(abilitySavedLevels != null && abilitySavedLevels.get(a.ability.name) != null){
                a.level = abilitySavedLevels.get(ag.name);
            }

            abilities.add(a);
            Bukkit.getServer().broadcastMessage("A: "+ag.name+" "+ag.toString());
            
        }
    }

    public void UpgradeAbility(String abilityName){
        //Bukkit.getServer().broadcastMessage("Trying to upgrage ability: " + abilityName);   
        for (AbilityPlayer a : abilities) {
            //Bukkit.getServer().broadcastMessage(" - search ability process: " + a.ability.name); 
            if(a.ability.name.equalsIgnoreCase(abilityName)){
                //Bukkit.getServer().broadcastMessage("Upgrading ability: " + a.ability.name); 
                int cost = (int)Math.sqrt(a.level)+1;
                if(abilityPoints >= cost){  
                    if(a.UpgradeAbility()){
                        abilityPoints-=cost;
                    }
                }
                break;
                
            }
        }
    }

    public void UseAbility(String abilityName){
        for (AbilityPlayer a : abilities) {
            if(a.ability.name.equalsIgnoreCase(abilityName)){
                a.UseAbility(player);
                break;
            }
        }
    }

    public void UpdAbilityVisible(SkillType st){
        int compareValue = 0;
            if(st == SkillType.combat) compareValue=combatLevel.level;
            else if(st == SkillType.building) compareValue=buildingLevel.level;
            else if(st == SkillType.farming) compareValue=farmingLevel.level;
            else if(st == SkillType.fishing) compareValue=fishingLevel.level;
            else if(st == SkillType.alchemy) compareValue=alchemyLevel.level;
            else if(st == SkillType.general) compareValue=generalLevel;
        
        //all skill list
        for (AbilityPlayer a : abilities) {
            if(a.ability.skillType == st){
                a.visible = compareValue >= a.ability.neededLevel;
            }
        }
    }

    public void SaveAbilitiesToDic(){
        abilitySavedLevels = new Hashtable<>();
        for (AbilityPlayer a : abilities) {
            abilitySavedLevels.put(a.ability.name, a.level);
        }
    }


}
