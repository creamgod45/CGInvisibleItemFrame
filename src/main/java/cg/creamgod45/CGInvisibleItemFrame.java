package cg.creamgod45;

import com.plotsquared.core.PlotSquared;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

public final class CGInvisibleItemFrame extends JavaPlugin implements CommandExecutor {
    public static JavaPlugin instance;
    public static CGInvisibleItemFrame THIS;
    public static Boolean plotsquared = false;
    public static FileConfiguration fileconfig;
    public static ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    public static CGInvisibleItemFrame getInstance() {
        return THIS;
    }

    public void updatecheck() {
        try {
            String json = "";
            URL url = new URL("https://www.creamgod45.lionfree.net/CGInvisibleItemFrame_version.txt");
            Scanner s = new Scanner(url.openStream(), "UTF-8");
            int k = 0;
            while (s.hasNext()) {
                if (k == 0) {
                    json = json + s.next();
                } else {
                    json = json + " " + s.next();
                }
                k++;
            }

            JSONObject jsonObject = new JSONObject(json);
            console.sendMessage(NMS.format("&b==============[" + ConfigReader.updatachecker_title + "]&b================"));
            if (!ConfigReader.version.equals(jsonObject.get("version").toString())) {
                console.sendMessage(NMS.format("&f⇒ " + ConfigReader.updatachecker_nowversion + " : " + ConfigReader.version));
                console.sendMessage(NMS.format("&f⇒ " + ConfigReader.updatachecker_newversion + " : " + jsonObject.get("version").toString()));

                if (ConfigReader.using_custom_message) {
                    Map<String, Object> custom = jsonObject.getJSONObject("update-suggestion").getJSONObject("custom").toMap();
                    for (Map.Entry<String, Object> ver : custom.entrySet()) {
                        int newversion = Integer.parseInt(ver.getKey());
                        int nowversion = Integer.parseInt(ConfigReader.version);
                        if (newversion >= nowversion) {
                            String s1 = ver.getValue().toString();
                            if (s1.length() >= 30) {
                                String s1a = s1.substring(0, (s1.length() / 2));
                                String s1b = s1.substring((s1.length() / 2));
                                console.sendMessage(NMS.format("&f⇒ &e" + ConfigReader.updatachecker_update_suggestion + " : " + s1a));
                                console.sendMessage(NMS.format("&e" + s1b));
                            } else {
                                console.sendMessage(NMS.format("&f⇒ &e" + ConfigReader.updatachecker_update_suggestion + " : " + ver.getValue().toString()));
                            }
                        }
                    }
                } else {
                    Map<String, Object> custom = jsonObject.getJSONObject("update-suggestion").getJSONObject("default").toMap();
                    for (Map.Entry<String, Object> ver : custom.entrySet()) {
                        int newversion = Integer.parseInt(ver.getKey());
                        int nowversion = Integer.parseInt(ConfigReader.version);
                        if (newversion >= nowversion) {
                            String s1 = ver.getValue().toString();
                            if (s1.length() >= 50) {
                                String s1a = s1.substring(0, (s1.length() / 2));
                                String s1b = s1.substring((s1.length() / 2));
                                console.sendMessage(NMS.format("&f⇒ &e" + ConfigReader.updatachecker_update_suggestion + " : " + s1a));
                                console.sendMessage(NMS.format("&e" + s1b));
                            } else {
                                console.sendMessage(NMS.format("&f⇒ &e" + ConfigReader.updatachecker_update_suggestion + " : " + ver.getValue().toString()));
                            }
                        }
                    }
                }

                console.sendMessage(NMS.format("&b============[" + ConfigReader.updatachecker_endtitle + "]&b=============="));
            } else {
                console.sendMessage(NMS.format("&f⇒ " + ConfigReader.updatachecker_done));
                console.sendMessage(NMS.format("&b============[" + ConfigReader.updatachecker_endtitle + "]&b=============="));
            }
            // read from your scanner
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException err) {
            System.out.println("Exception : " + err.toString());
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        CGInvisibleItemFrame.THIS = this;

        updatecheck();
        fileconfig = this.getConfig();
        ConfigReader.load();

        this.getServer().getPluginManager().registerEvents((Listener) new ItemFrameListener(), (Plugin) this);
        console.sendMessage(ConfigReader.on_enable);

        console.sendMessage(ConfigReader.on_dectect_plotsquared);
        if (Bukkit.getPluginManager().getPlugin("PlotSquared") != null) {
            plotsquared = true;
            Bukkit.getScheduler().runTaskLater(this, () -> console.sendMessage(ConfigReader.on_dectect_plotsquared_done), 20);
            Bukkit.getScheduler().runTaskLater(this, () -> console.sendMessage(NMS.format(ConfigReader.Prefix + "&2PlotSquared Version&r&2:[" + PlotSquared.get().getVersion().versionString() + "]")), 20);
        } else {
            console.sendMessage(ConfigReader.on_dectect_plotsquared_noinstall);
        }
        if (Bukkit.getPluginManager().getPlugin("InvisibleItemFrame") != null) {
            console.sendMessage(ConfigReader.on_dectect_conflict);
            this.getPluginLoader().disablePlugin(this);
        }

        new Metrics(CGInvisibleItemFrame.getInstance(), 12701);
    }

    @Override
    public void onLoad() {
        File config = new File("plugins/CGInvisibleItemFrame/config.yml");
        fileconfig = this.getConfig();

        if (!(config.exists())) {
            Bukkit.getServer().getConsoleSender().sendMessage(ConfigReader.on_load);
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        } else ConfigReader.load();

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
        try (InputStream in = new FileInputStream(source); OutputStream out = new FileOutputStream(target)) {
            byte[] buf = new byte[1024];
            int length;
            while ((length = in.read(buf)) > 0) {
                out.write(buf, 0, length);
            }
        }
    }

    public void update_config() {
        console.sendMessage("");
        console.sendMessage("");
        console.sendMessage("");
        SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        String timeStamp = date.format(new Date());

        File oldconfig = new File("plugins/CGInvisibleItemFrame/config.yml");
        File backup = new File("plugins/CGInvisibleItemFrame/backups/");
        File newconfig = new File("plugins/CGInvisibleItemFrame/backups/config_bak_" + timeStamp + ".yml");
        File lock = new File("plugins/CGInvisibleItemFrame/config.lock");
        try {
            if (backup.mkdirs() || backup.exists()) {
                if (ConfigReader.update_config) {
                    copy(oldconfig, newconfig);
                    console.sendMessage(ConfigReader.file_configbak_copyed + "config_bak_" + timeStamp + ".yml");
                    if (oldconfig.delete()) {
                        console.sendMessage(ConfigReader.file_config_deleted);
                        getConfig().options().copyDefaults(true);
                        saveDefaultConfig();
                        console.sendMessage(ConfigReader.on_load);
                    }
                    if (!lock.exists() && lock.createNewFile()) {
                        console.sendMessage(ConfigReader.file_config_lock);

                    }
                    return;
                } else if (lock.exists()) {
                    console.sendMessage(ConfigReader.file_config_locked);
                } else {
                    copy(oldconfig, newconfig);
                    console.sendMessage(ConfigReader.file_configbak_copyed + "config_bak_" + timeStamp + ".yml");
                    if (oldconfig.delete()) {
                        console.sendMessage(ConfigReader.file_config_deleted);
                        getConfig().options().copyDefaults(true);
                        saveDefaultConfig();
                        console.sendMessage(ConfigReader.on_load);
                    }
                    if (lock.createNewFile()) {
                        console.sendMessage(ConfigReader.file_config_lock);

                    }
                    return;
                }
            }
        } catch (IOException e) {
            console.sendMessage(ConfigReader.file_io_error);
            e.printStackTrace();
        }
    }

    public Boolean HasPermission(Player player, String Permission) {
        if (Permission == null) {
            Permission = "";
        }
        if (player.isOp()) return true;
        else return player.hasPermission(Permission);
    }

    @Override
    public void onDisable() {
        this.getServer().getConsoleSender().sendMessage(ConfigReader.on_disable);
        if (!ConfigReader.using_custom_message) {
            this.getServer().getConsoleSender().sendMessage(NMS.format(ConfigReader.Prefix + "&b&l謝謝你使用我的插件 :)"));
        } else {
            this.getServer().getConsoleSender().sendMessage(NMS.format(ConfigReader.Prefix + "&b&lThank you for using my plugin :)"));
        }
    }
}
