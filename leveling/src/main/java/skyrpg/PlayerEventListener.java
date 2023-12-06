package skyrpg;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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

import skyrpg.Skill.SkillType;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

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





}