package skyrpg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PluginSaver {
    Plugin plugin;
    File dir;
    File profilesDir;


    public PluginSaver(Plugin p){
        plugin = p;

        dir = plugin.getDataFolder();
        if(!dir.exists()) dir.mkdir();

        profilesDir = new File(dir + "/profiles");
        if(!profilesDir.exists()) profilesDir.mkdir();  
    }

    public void save(Object o, File f){
        try{
            if(!f.exists()) 
            {
                f.createNewFile();
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(o);
            oos.flush();
            oos.close();

        } catch(Exception e) {e.printStackTrace();}
    }

    public Object load(String f){
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            Object res = ois.readObject();
            ois.close();
            return res;

        } catch(Exception e) {return null;}
    }

    




}
