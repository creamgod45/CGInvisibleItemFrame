package cg.creamgod45;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ConfigReader extends JavaPlugin {
    // Config
    public static String Prefix = "&5&l物品展示架輔助》 &r";
    public static Boolean using_custom_message = false;
    public static Boolean debugConsole_msg_default_op = false;
    public static Boolean debugPlayer_msg_default_op = false;
    public static List<String> PlotSquared_Worlds;
    public static Boolean fast_deny_player_use= false;
    public static List<String> fast_deny_player_use_worlds;
    public static List<String> disable_worlds;
    // Messages
    public static String on_load = format(Prefix + "&2已複製 config.yml 至插件資料夾。");
    public static String on_enable = format(Prefix + "&2CGInvisibleItemFrame 啟動成功");
    public static String on_disable = format(Prefix + "&4CGInvisibleItemFrame 關閉成功");
    public static String on_dectect_plotsquared = format(Prefix + "&e偵測 PlotSquared Service...");
    public static String on_dectect_plotsquared_done = format(Prefix + "&e偵測 PlotSquared Service...&aDone");
    public static String no_permission = format(Prefix + "&c沒有權限使用此操作!!");
    public static String plotsquared_worlds_noset_warning = format(Prefix + "&c如果 PlotSquared_Worlds 未設定相關的 PlotSquared 的地圖，則 PlotSquared 的地圖將不受此插件的保護!!");
    public static String debug_player_plot_isowner = format(Prefix + "&2你是此地點的地主");
    public static String debug_player_plot_istrust =format(Prefix + "&2你是此地點的信任夥伴");
    public static String debug_player_plot_no = format(Prefix + "&c你不是此地點的地主或是信任夥伴");
    public static String debug_player_plot_inRoad = format(Prefix + "&c你站在路上所以無法變更物品展示框的狀態");
    public static String debug_check_plot_permission = format(Prefix + "&c玩家 *player_name* 沒有 cginvisibleitemframe.use 權限");
    public static String debug_check_plot_inRoad = format(Prefix + "&e玩家 *player_name* 站在路上，所以不能改變任何物品展示框的狀態");
    public static String debug_check_plot_isowner = format(Prefix + "&e玩家 *player_name* 是此地點的地主");
    public static String debug_check_plot_istrust = format(Prefix + "&e玩家 *player_name* 是此地點的信任夥伴");
    public static String debug_check_plot_no = format(Prefix + "&e玩家 *player_name* 不是此地點的地主或是信任夥伴");

    public static String getstr(String path, Boolean placeholder){
        FileConfiguration _instance = CGInvisibleItemFrame.fileconfig;
        if(placeholder) return format(_instance.getString(path));
        if(_instance.getString(path) == "" || _instance.getString(path) == null) {
            if(getdefault(path).equals(path)){
                return path;
            }else{
                return format(getdefault(path));
            }
        }
        return format(_instance.getString(path));
    }

    public static String getdefault(String path){
        switch (path){
            case "Setting.prefix":
                return Prefix;
            case "Messages.on_load":
                return on_load;
            case "Messages.on_enable":
                return on_enable;
            case "Messages.on_disable":
                return on_disable;
            case "Messages.on_dectect_plotsquared":
                return on_dectect_plotsquared;
            case "Messages.on_dectect_plotsquared_done":
                return on_dectect_plotsquared_done;
            case "Messages.no_permission":
                return no_permission;
            case "Messages.plotsquared_worlds_noset_warning":
                return plotsquared_worlds_noset_warning;
            case "Messages.debug_player_plot_isowner":
                return debug_player_plot_isowner;
            case "Messages.debug_player_plot_istrust":
                return debug_player_plot_istrust;
            case "Messages.debug_player_plot_no":
                return debug_player_plot_no;
            case "Messages.debug_player_plot_inRoad":
                return debug_player_plot_inRoad;
            case "Messages.debug_check_plot_permission":
                return debug_check_plot_permission;
            case "Messages.debug_check_plot_inRoad":
                return debug_check_plot_inRoad;
            case "Messages.debug_check_plot_isowner":
                return debug_check_plot_isowner;
            case "Messages.debug_check_plot_istrust":
                return debug_check_plot_istrust;
            case "Messages.debug_check_plot_no":
                return debug_check_plot_no;
            default:
                return path;
        }
    }

    public static void load(){
        FileConfiguration _instance = CGInvisibleItemFrame.fileconfig;
        Prefix = getstr("Setting.prefix", false);
        using_custom_message = _instance.getBoolean("Setting.using_custom_message");
        debugConsole_msg_default_op = _instance.getBoolean("Setting.debugConsole_msg_default_op");
        debugPlayer_msg_default_op = _instance.getBoolean("Setting.debugPlayer_msg_default_op");
        PlotSquared_Worlds = _instance.getStringList("Setting.PlotSquared_Worlds");
        fast_deny_player_use = _instance.getBoolean("Setting.fast_deny_player_use");
        fast_deny_player_use_worlds = _instance.getStringList("Setting.fast_deny_player_use_worlds");
        disable_worlds = _instance.getStringList("Setting.disable_worlds");

        if(using_custom_message){
            on_load = Prefix + getstr("Messages.on_load", false);
            on_enable = Prefix + getstr("Messages.on_enable", false);
            on_disable = Prefix + getstr("Messages.on_disable", false);
            on_dectect_plotsquared = Prefix + getstr("Messages.on_dectect_plotsquared", false);
            on_dectect_plotsquared_done = Prefix + getstr("Messages.on_dectect_plotsquared_done", false);
            no_permission = Prefix + getstr("Messages.no_permission", false);
            plotsquared_worlds_noset_warning = Prefix + getstr("Messages.plotsquared_worlds_noset_warning", false);
            debug_player_plot_isowner = Prefix + getstr("Messages.debug_player_plot_isowner", false);
            debug_player_plot_istrust = Prefix + getstr("Messages.debug_player_plot_istrust", false);
            debug_player_plot_no = Prefix + getstr("Messages.debug_player_plot_no", false);
            debug_player_plot_inRoad = Prefix + getstr("Messages.debug_player_plot_inRoad", false);
            debug_check_plot_permission = Prefix + getstr("Messages.debug_check_plot_permission", true);
            debug_check_plot_inRoad = Prefix + getstr("Messages.debug_check_plot_inRoad", true);
            debug_check_plot_isowner = Prefix + getstr("Messages.debug_check_plot_isowner", true);
            debug_check_plot_istrust = Prefix + getstr("Messages.debug_check_plot_istrust", true);
            debug_check_plot_no = Prefix + getstr("Messages.debug_check_plot_no", true);
        }

    }

    // by haer0248 URL:https://github.com/haer0248/ServerCore/blob/main/src/me/haer0248/ServerCore/Main.java:297~299
    public static String format(String format){
        return ChatColor.translateAlternateColorCodes('&', format);
    }
}
