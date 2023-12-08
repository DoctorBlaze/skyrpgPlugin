package skyrpg;

import org.bukkit.Bukkit;
import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Crops;

import net.md_5.bungee.api.ChatColor;
import skyrpg.Skill.SkillType;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import org.bukkit.event.block.BlockBreakEvent;

//import org.bukkit.event.player.PlayerMot;


public class PlayerEventListener implements Listener{
    public PlayerEventListener(Plugin pl){
        plugin = pl;
    }
    
    public Plugin plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        plugin.LoadPlayer(event);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        plugin.SavePlayer(event);
    }




    public void onPlayerKillsEntity(Entity dead, Player killer){

        plugin.profiles.get(killer.getName()).combatLevel.AddScore(100);

    }


    @EventHandler
    public void onEntityDies(EntityDeathEvent e) {
        LivingEntity dead = e.getEntity();
        Player killer = dead.getKiller();

        if (!(dead instanceof Player)) {
            onPlayerKillsEntity(dead, killer);
        }
    }

     @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(p.getInventory().getItemInMainHand().getType() == Material.BOOK){
            plugin.profiles.get(p.getName()).ViewProfile();
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        InventoryView view = event.getView();
        
        
        if(view.getTitle().equals("Skills")){
            if(event.getCurrentItem()==null) return;
            Inventory inv = view.getTopInventory();
            SkillType st;
            String selName = event.getCurrentItem().getItemMeta().getDisplayName();

            if(selName.contains("General")) st = SkillType.general;
            else if(selName.contains("Building")) st = SkillType.building;
            else if(selName.contains("Combat")) st = SkillType.combat;
            else if(selName.contains("Farming")) st = SkillType.farming;
            else if(selName.contains("Fishing")) st = SkillType.fishing;
            else if(selName.contains("Alchemy")) st = SkillType.alchemy;
            else st = null;

            if(st != null){
                plugin.profiles.get(view.getPlayer().getName())._PrintAbilities(inv, st);
                event.setCancelled(true);
                return;
            }
            //plugin.profiles.get(view.getPlayer().getName())._PrintAbilities(inv, st);
            PlayerProfile pp = plugin.profiles.get(view.getPlayer().getName());
            pp.UpgradeAbility(selName);
            pp._PrintAbilities(inv, pp.displayedSkill);
            event.setCancelled(true);
            //ItemStack item = event.getCurrentItem();
            //ItemMeta iMeta = item.getItemMeta();

            //iMeta
            //Bukkit.getServer().broadcastMessage(selName);
        }
    }


    @EventHandler
    void onPlayerSneaks(PlayerToggleSneakEvent event){
        Player p = event.getPlayer();
        PlayerProfile pp = plugin.profiles.get(p.getName());
        if(p.isSprinting() && p.getFallDistance() > 0) pp.UseAbility("Dash");

        if(p.getVelocity().getY() > 0.01f && !p.isOnGround()) pp.UseAbility("Double Jump");
    }



    @EventHandler
    void onPlayerBreaksBlock(BlockBreakEvent event){
        Player p = event.getPlayer();
        if(p == null) return;

        int fxp = GetFarmingXP(event.getBlock());
        if(fxp > 0) plugin.profiles.get(p.getName()).UseAbility("Farm Sprint");
        plugin.profiles.get(p.getName()).farmingLevel.AddScore(fxp);
    }
    


    public int GetFarmingXP(Block block) {
        BlockState bs = block.getState();
        if(block.getType() == Material.WHEAT || block.getType() == Material.POTATOES || block.getType() == Material.CARROTS || block.getType() == Material.BEETROOTS){
           if(bs.getBlockData() instanceof Ageable){
                Ageable ageable = (Ageable)bs.getBlockData();
                if(ageable.getAge() >= ageable.getMaximumAge()) { return 2; }
           }
           else{
            Bukkit.getServer().broadcastMessage(ChatColor.RED+"This is not ageable");
           }
        }
        return 0;
    } 


}