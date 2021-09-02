package cg.creamgod45;

import com.plotsquared.core.PlotSquared;
import com.sk89q.jchronic.utils.Time;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.enginehub.piston.config.Config;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class CGInvisibleItemFrame extends JavaPlugin implements CommandExecutor {
    public static JavaPlugin instance;
    public static CGInvisibleItemFrame THIS;
    public static Boolean plotsquared = false;
    public static FileConfiguration fileconfig;
    public static ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    @Override
    public void onEnable() {
        instance = this;
        CGInvisibleItemFrame.THIS = this;

        fileconfig = this.getConfig();
        ConfigReader.load();

        this.getServer().getPluginManager().registerEvents((Listener)new ItemFrameListener(), (Plugin)this);
        console.sendMessage(ConfigReader.on_enable);

        console.sendMessage(ConfigReader.on_dectect_plotsquared);
        if (Bukkit.getPluginManager().getPlugin("PlotSquared") != null) {
            plotsquared = true;
            console.sendMessage(ConfigReader.on_dectect_plotsquared_done);
            console.sendMessage(NMS.format(ConfigReader.Prefix + "&2PlotSquared Version&r&2:[" + PlotSquared.get().getVersion().versionString()+"]"));
        }else {
            console.sendMessage(ConfigReader.on_dectect_plotsquared_noinstall);
        }
        if (Bukkit.getPluginManager().getPlugin("InvisibleItemFrame") != null){
            console.sendMessage(ConfigReader.on_dectect_conflict);
            this.getPluginLoader().disablePlugin(this);
        }
    }

    @Override
    public void onLoad() {
        File config = new File("plugins/CGInvisibleItemFrame/config.yml");
        fileconfig = this.getConfig();

        if (!(config.exists())){
            Bukkit.getServer().getConsoleSender().sendMessage(ConfigReader.on_load);
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }else ConfigReader.load();

        update_config();
    }


    public void copy(File sourceLocation, File targetLocation) throws IOException {
        if (sourceLocation.isDirectory()) {
            copyDirectory(sourceLocation, targetLocation);
        } else {
            copyFile(sourceLocation, targetLocation);
        }
    }

    private void copyDirectory(File source, File target) throws IOException {
        if (!target.exists()) {
            target.mkdir();
        }

        for (String f : source.list()) {
            copy(new File(source, f), new File(target, f));
        }
    }

    private void copyFile(File source, File target) throws IOException {
        try (
                InputStream in = new FileInputStream(source);
                OutputStream out = new FileOutputStream(target)) {
            byte[] buf = new byte[1024];
            int length;
            while ((length = in.read(buf)) > 0) {
                out.write(buf, 0, length);
            }
        }
    }

    public void update_config () {
        if(this.getConfig().getString("Setting.version")=="120") return;
        console.sendMessage(this.getConfig().getString("Setting.version"));
        File oldconfig = new File("plugins/CGInvisibleItemFrame/config.yml");
        File newconfig = new File("plugins/CGInvisibleItemFrame/config_bak.yml");
        try {
            if(newconfig.exists()) {
                console.sendMessage(ConfigReader.file_configbak_exists);
            }
            if(newconfig.createNewFile()) {
                console.sendMessage(ConfigReader.file_configbak_created);
                copy(oldconfig, newconfig);
                console.sendMessage(ConfigReader.file_configbak_copyed);

                if(oldconfig.delete()){
                    console.sendMessage(ConfigReader.file_config_deleted);
                }
                getConfig().options().copyDefaults(true);
                saveDefaultConfig();
                console.sendMessage(ConfigReader.on_load);
            }
        } catch (IOException e) {
            console.sendMessage(ConfigReader.file_io_error);
            e.printStackTrace();
        }
    }

    public Boolean HasPermission(Player player, String Permission){
        if(Permission == null){Permission = "" ;}
        if(player.isOp()) return true; else return player.hasPermission(Permission);
    }

    @Override
    public void onDisable() {
        this.getServer().getConsoleSender().sendMessage(ConfigReader.on_disable);
        if(!ConfigReader.using_custom_message){
            this.getServer().getConsoleSender().sendMessage(NMS.format(ConfigReader.Prefix + "&b&l謝謝你使用我的插件 :)"));
        }else{
            this.getServer().getConsoleSender().sendMessage(NMS.format(ConfigReader.Prefix + "&b&lThank you for using my plugin :)"));
        }
    }
}
