package cg.creamgod45;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigReader {
    // Config
    public static String version = "132";
    public static Boolean update_config = false;
    public static String Prefix = "&5&l物品展示架輔助》 &r";
    public static Boolean using_custom_message = false;
    public static Boolean debugConsole_msg_default_op = false;
    public static Boolean debugPlayer_msg_default_op = false;
    public static List<String> PlotSquared_Worlds;
    public static Boolean fast_deny_player_use= false;
    public static List<String> fast_deny_player_use_worlds;
    public static List<String> disable_worlds;
    // Messages
    public static String on_load                          = format(Prefix + "&2已複製 config.yml 至插件資料夾。");
    public static String on_enable                        = format(Prefix + "&2CGInvisibleItemFrame 啟動成功");
    public static String on_disable                       = format(Prefix + "&4CGInvisibleItemFrame 關閉成功");
    public static String on_dectect_conflict              = format(Prefix + "&4警告此插件 InvisibleItemFrame 相互衝突!!&c建議移除");
    public static String on_dectect_plotsquared           = format(Prefix + "&e偵測 PlotSquared Service...");
    public static String on_dectect_plotsquared_done      = format(Prefix + "&e偵測 PlotSquared Service...&a完成");
    public static String on_dectect_plotsquared_noinstall = format(Prefix + "&e偵測 PlotSquared Service...&e沒有安裝");
    public static String no_permission                    = format(Prefix + "&c沒有權限使用此操作!!");
    public static String plotsquared_worlds_noset_warning = format(Prefix + "&c如果 PlotSquared_Worlds 未設定相關的 PlotSquared 的地圖，則 PlotSquared 的地圖將不受此插件的保護!!");
    public static String debug_player_plot_isowner        = format(Prefix + "&2你是此地點的地主");
    public static String debug_player_plot_istrust        = format(Prefix + "&2你是此地點的信任夥伴");
    public static String debug_player_plot_no             = format(Prefix + "&c你不是此地點的地主或是信任夥伴");
    public static String debug_player_plot_inRoad         = format(Prefix + "&c你站在路上所以無法變更物品展示框的狀態");
    public static String debug_check_plot_permission      = format(Prefix + "&c玩家 *player_name* 沒有 cginvisibleitemframe.use 權限");
    public static String debug_check_plot_inRoad          = format(Prefix + "&e玩家 *player_name* 站在路上，所以不能改變任何物品展示框的狀態");
    public static String debug_check_plot_isowner         = format(Prefix + "&e玩家 *player_name* 是此地點的地主");
    public static String debug_check_plot_istrust         = format(Prefix + "&e玩家 *player_name* 是此地點的信任夥伴");
    public static String debug_check_plot_no              = format(Prefix + "&e玩家 *player_name* 不是此地點的地主或是信任夥伴");
    public static String file_configbak_copyed            = format(Prefix + "&e檔案 config.yml 已經備份到 /CGInvisibleItemFrame/backups/");
    public static String file_config_deleted              = format(Prefix + "&c檔案 config.yml 已刪除");
    public static String file_io_error                    = format(Prefix + "&4檔案 IO 錯誤");
    public static String file_config_lock                 = format(Prefix + "&f[&4鎖定&f]&e設定檔案 config.yml");
    public static String file_config_locked               = format(Prefix + "&c設定檔案 config.yml 已經被鎖定。");
    public static String updatachecker_title              = format("&b更新檢查");
    public static String updatachecker_endtitle           = format("&b更新檢查完成");
    public static String updatachecker_nowversion         = format(Prefix + "&f[&c舊&f] &c現在版本");
    public static String updatachecker_newversion         = format(Prefix + "&f[&a新&f] &a最新版本");
    public static String updatachecker_update_suggestion  = format(Prefix + "&f[&a新&f] &e更新建議");
    public static String updatachecker_done               = format(Prefix + "&a你已經是最新版本");


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
            case "Messages.on_dectect_conflict":
                return on_dectect_conflict;
            case "Messages.on_dectect_plotsquared":
                return on_dectect_plotsquared;
            case "Messages.on_dectect_plotsquared_done":
                return on_dectect_plotsquared_done;
            case "Messages.on_dectect_plotsquared_noinstall":
                return on_dectect_plotsquared_noinstall;
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
            case "Messages.file_configbak_copyed":
                return file_configbak_copyed;
            case "Messages.file_config_deleted":
                return file_config_deleted;
            case "Messages.file_io_error":
                return file_io_error;
            case "Messages.updatachecker_title":
                return updatachecker_title;
            case "Messages.updatachecker_endtitle":
                return updatachecker_endtitle;
            case "Messages.updatachecker_nowversion":
                return updatachecker_nowversion;
            case "Messages.updatachecker_newversion":
                return updatachecker_newversion;
            case "Messages.updatachecker_update_suggestion":
                return updatachecker_update_suggestion;
            case "Messages.updatachecker_done":
                return updatachecker_done;
            case "Messages.file_config_lock":
                return file_config_lock;  
            case "Messages.file_config_locked":
                return file_config_locked;  
            default:
                return path;
        }
    }

    public static void load(){
        FileConfiguration _instance = CGInvisibleItemFrame.fileconfig;
        Prefix = getstr("Setting.prefix", false);
        update_config = _instance.getBoolean("Setting.update_config");
        using_custom_message = _instance.getBoolean("Setting.using_custom_message");
        debugConsole_msg_default_op = _instance.getBoolean("Setting.debugConsole_msg_default_op");
        debugPlayer_msg_default_op = _instance.getBoolean("Setting.debugPlayer_msg_default_op");
        PlotSquared_Worlds = _instance.getStringList("Setting.PlotSquared_Worlds");
        fast_deny_player_use = _instance.getBoolean("Setting.fast_deny_player_use");
        fast_deny_player_use_worlds = _instance.getStringList("Setting.fast_deny_player_use_worlds");
        disable_worlds = _instance.getStringList("Setting.disable_worlds");

        if(using_custom_message){
            on_load                          = Prefix + getstr("Messages.on_load", false);
            on_enable                        = Prefix + getstr("Messages.on_enable", false);
            on_disable                       = Prefix + getstr("Messages.on_disable", false);
            on_dectect_conflict              = Prefix + getstr("Messages.on_dectect_conflict", false);
            on_dectect_plotsquared           = Prefix + getstr("Messages.on_dectect_plotsquared", false);
            on_dectect_plotsquared_done      = Prefix + getstr("Messages.on_dectect_plotsquared_done", false);
            on_dectect_plotsquared_noinstall = Prefix + getstr("Messages.on_dectect_plotsquared_noinstall", false);
            no_permission                    = Prefix + getstr("Messages.no_permission", false);
            plotsquared_worlds_noset_warning = Prefix + getstr("Messages.plotsquared_worlds_noset_warning", false);
            debug_player_plot_isowner        = Prefix + getstr("Messages.debug_player_plot_isowner", false);
            debug_player_plot_istrust        = Prefix + getstr("Messages.debug_player_plot_istrust", false);
            debug_player_plot_no             = Prefix + getstr("Messages.debug_player_plot_no", false);
            debug_player_plot_inRoad         = Prefix + getstr("Messages.debug_player_plot_inRoad", false);
            debug_check_plot_permission      = Prefix + getstr("Messages.debug_check_plot_permission", true);
            debug_check_plot_inRoad          = Prefix + getstr("Messages.debug_check_plot_inRoad", true);
            debug_check_plot_isowner         = Prefix + getstr("Messages.debug_check_plot_isowner", true);
            debug_check_plot_istrust         = Prefix + getstr("Messages.debug_check_plot_istrust", true);
            debug_check_plot_no              = Prefix + getstr("Messages.debug_check_plot_no", true);
            file_configbak_copyed            = Prefix + getstr("Messages.file_configbak_copyed", false);
            file_config_deleted              = Prefix + getstr("Messages.file_config_deleted", false);
            file_io_error                    = Prefix + getstr("Messages.file_io_error", false);
            file_config_lock                 = Prefix + getstr("Messages.file_config_lock", false);  
            file_config_locked               = Prefix + getstr("Messages.file_config_locked", false);
            updatachecker_title              = getstr("Messages.updatachecker_title", false);
            updatachecker_endtitle           = getstr("Messages.updatachecker_endtitle", false);
            updatachecker_nowversion         = Prefix + getstr("Messages.updatachecker_nowversion", false);
            updatachecker_newversion         = Prefix + getstr("Messages.updatachecker_newversion", false);
            updatachecker_update_suggestion  = Prefix + getstr("Messages.updatachecker_update_suggestion", false);
            updatachecker_done               = Prefix + getstr("Messages.updatachecker_done",false);
        }

    }

    // by haer0248 URL:https://github.com/haer0248/ServerCore/blob/main/src/me/haer0248/ServerCore/Main.java:297~299
    public static String format(String format){
        return ChatColor.translateAlternateColorCodes('&', format);
    }
}
