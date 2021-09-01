package cg.creamgod45;

import com.plotsquared.core.PlotSquared;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public final class CGInvisibleItemFrame extends JavaPlugin implements CommandExecutor {
    public static JavaPlugin instance;
    public static CGInvisibleItemFrame THIS;
    public static Boolean plotsquared = false;
    public static FileConfiguration fileconfig;

    @Override
    public void onEnable() {
        instance = this;
        CGInvisibleItemFrame.THIS = this;

        fileconfig = this.getConfig();
        ConfigReader.load();

        this.getServer().getPluginManager().registerEvents((Listener)new ItemFrameListener(), (Plugin)this);
        this.getServer().getConsoleSender().sendMessage(ConfigReader.on_enable);

        Bukkit.getServer().getConsoleSender().sendMessage(ConfigReader.on_dectect_plotsquared);
        if (Bukkit.getPluginManager().getPlugin("PlotSquared") != null) {
            plotsquared = true;
            Bukkit.getServer().getConsoleSender().sendMessage(ConfigReader.on_dectect_plotsquared_done);
            Bukkit.getServer().getConsoleSender().sendMessage(NMS.format(ConfigReader.Prefix + "&2&lPlotSquared Version&r&2:[" + PlotSquared.get().getVersion().versionString()+"]"));
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
